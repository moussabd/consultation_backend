#!/bin/bash
# ============================================================================
# SCRIPT: test-local.sh
# DESCRIPTION: Teste l'image Docker localement
# USAGE: ./test-local.sh
# ============================================================================

echo "╔════════════════════════════════════════════════════════════╗"
echo "║           TEST LOCAL DOCKER                               ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# -----------------------------------------------------------------
# Variables
# -----------------------------------------------------------------
IMAGE_NAME="consultation-backend:latest"
CONTAINER_NAME="consultation-local-test"
PORT_HOST="10001"
PORT_CONTAINER="10001"
NETWORK_NAME="consultation-network"

# -----------------------------------------------------------------
# Nettoyage préalable
# -----------------------------------------------------------------
echo "🧹 NETTOYAGE PRÉALABLE"
echo "---------------------"

# Arrêter et supprimer le conteneur existant
if docker ps -a --filter "name=$CONTAINER_NAME" | grep -q $CONTAINER_NAME; then
    echo "🛑 Arrêt du conteneur existant: $CONTAINER_NAME"
    docker stop $CONTAINER_NAME > /dev/null
    docker rm $CONTAINER_NAME > /dev/null
    echo "✅ Conteneur supprimé"
else
    echo "ℹ️  Aucun conteneur existant trouvé"
fi

# Supprimer le réseau existant
if docker network ls | grep -q $NETWORK_NAME; then
    echo "🌐 Suppression du réseau existant: $NETWORK_NAME"
    docker network rm $NETWORK_NAME > /dev/null
fi

# -----------------------------------------------------------------
# Vérification de l'image
# -----------------------------------------------------------------
echo ""
echo "🔍 VÉRIFICATION DE L'IMAGE"
echo "--------------------------"

if ! docker image inspect $IMAGE_NAME > /dev/null 2>&1; then
    echo "❌ Image non trouvée: $IMAGE_NAME"
    echo ""
    echo "📋 Solutions:"
    echo "   1. Construisez l'image: ./build-docker-jawk.sh"
    echo "   2. Vérifiez les images disponibles: docker images"
    exit 1
fi

echo "✅ Image trouvée: $IMAGE_NAME"
IMAGE_SIZE=$(docker image inspect $IMAGE_NAME --format='{{.Size}}')
IMAGE_SIZE_MB=$((IMAGE_SIZE / 1024 / 1024))
echo "📏 Taille de l'image: ${IMAGE_SIZE_MB} MB"

# -----------------------------------------------------------------
# Création du réseau Docker
# -----------------------------------------------------------------
echo ""
echo "🌐 CRÉATION DU RÉSEAU DOCKER"
echo "---------------------------"

if docker network create $NETWORK_NAME; then
    echo "✅ Réseau créé: $NETWORK_NAME"
else
    echo "⚠️  Impossible de créer le réseau, utilisation du réseau par défaut"
    NETWORK_NAME=""
fi

# -----------------------------------------------------------------
# Démarrage du conteneur
# -----------------------------------------------------------------
echo ""
echo "🚀 DÉMARRAGE DU CONTENEUR"
echo "------------------------"

echo "▶️  Démarrage en cours..."
echo "   Image: $IMAGE_NAME"
echo "   Port hôte: $PORT_HOST"
echo "   Port conteneur: $PORT_CONTAINER"
echo "   Nom du conteneur: $CONTAINER_NAME"
echo ""

# Variables d'environnement
ENV_VARS="-e PORT=$PORT_CONTAINER"
ENV_VARS="$ENV_VARS -e SPRING_PROFILES_ACTIVE=dev"
ENV_VARS="$ENV_VARS -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb"
ENV_VARS="$ENV_VARS -e SPRING_DATASOURCE_USERNAME=sa"
ENV_VARS="$ENV_VARS -e SPRING_DATASOURCE_PASSWORD="

# Démarrer le conteneur
if [ -n "$NETWORK_NAME" ]; then
    NETWORK_OPT="--network $NETWORK_NAME"
else
    NETWORK_OPT=""
fi

docker run -d \
    --name $CONTAINER_NAME \
    -p $PORT_HOST:$PORT_CONTAINER \
    $NETWORK_OPT \
    $ENV_VARS \
    $IMAGE_NAME

if [ $? -ne 0 ]; then
    echo "❌ ÉCHEC DU DÉMARRAGE"
    exit 1
fi

echo "✅ Conteneur démarré avec succès"

# -----------------------------------------------------------------
# Attente du démarrage de l'application
# -----------------------------------------------------------------
echo ""
echo "⏳ ATTENTE DU DÉMARRAGE DE L'APPLICATION"
echo "---------------------------------------"

echo "🔄 Vérification de l'état..."
MAX_WAIT=60
WAITED=0

while [ $WAITED -lt $MAX_WAIT ]; do
    # Vérifier si le conteneur est en cours d'exécution
    if ! docker ps --filter "name=$CONTAINER_NAME" --filter "status=running" | grep -q $CONTAINER_NAME; then
        echo "❌ Le conteneur s'est arrêté"
        echo ""
        echo "📝 Derniers logs:"
        docker logs $CONTAINER_NAME --tail 20
        exit 1
    fi
    
    # Vérifier si l'application répond
    if curl -s "http://localhost:$PORT_HOST/actuator/health" > /dev/null 2>&1; then
        echo "✅ Application démarrée et répond aux requêtes"
        break
    fi
    
    echo -n "."
    sleep 1
    WAITED=$((WAITED + 1))
done

if [ $WAITED -ge $MAX_WAIT ]; then
    echo ""
    echo "⚠️  Délai d'attente dépassé, application peut être lente"
fi

# -----------------------------------------------------------------
# Affichage des informations
# -----------------------------------------------------------------
echo ""
echo "📊 INFORMATIONS DU CONTENEUR"
echo "---------------------------"

# État du conteneur
echo "🔍 État:"
docker ps --filter "name=$CONTAINER_NAME" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Image}}"

# Logs récents
echo ""
echo "📝 LOGS RÉCENTS (20 dernières lignes):"
echo "------------------------------------"
docker logs $CONTAINER_NAME --tail 20

# Utilisation des ressources
echo ""
echo "💾 UTILISATION DES RESSOURCES:"
echo "-----------------------------"
docker stats $CONTAINER_NAME --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.MemPerc}}"

# -----------------------------------------------------------------
# Test des endpoints
# -----------------------------------------------------------------
echo ""
echo "🧪 TESTS DES ENDPOINTS"
echo "--------------------"

echo "🔗 URL de test: http://localhost:$PORT_HOST"
echo ""

# Test de l'endpoint health
echo "1. Test /actuator/health:"
if curl -s "http://localhost:$PORT_HOST/actuator/health" | grep -q '"status":"UP"'; then
    echo "   ✅ HEALTH: UP"
else
    HEALTH_RESPONSE=$(curl -s "http://localhost:$PORT_HOST/actuator/health")
    echo "   ❌ HEALTH: $HEALTH_RESPONSE"
fi

# Test de l'endpoint info
echo ""
echo "2. Test /actuator/info:"
INFO_RESPONSE=$(curl -s "http://localhost:$PORT_HOST/actuator/info")
if [ -n "$INFO_RESPONSE" ] && [ "$INFO_RESPONSE" != "{}" ]; then
    echo "   ✅ INFO: Disponible"
else
    echo "   ℹ️  INFO: Non configuré"
fi

# -----------------------------------------------------------------
# Instructions
# -----------------------------------------------------------------
echo ""
echo "🎉 TEST LOCAL RÉUSSI !"
echo ""
echo "📋 INSTRUCTIONS:"
echo "   👀 Voir les logs en temps réel: docker logs -f $CONTAINER_NAME"
echo "   📊 Voir les stats: docker stats $CONTAINER_NAME"
echo "   🐚 Ouvrir un shell: docker exec -it $CONTAINER_NAME sh"
echo "   🛑 Arrêter: docker stop $CONTAINER_NAME"
echo "   🗑️  Supprimer: docker rm $CONTAINER_NAME"
echo ""
echo "🌐 APPLICATION ACCESSIBLE SUR:"
echo "   http://localhost:$PORT_HOST"
echo "   http://localhost:$PORT_HOST/actuator/health"
echo ""
echo "✅ TEST TERMINÉ"