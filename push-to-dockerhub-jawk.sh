#!/bin/bash
# ============================================================================
# SCRIPT: push-to-dockerhub-jawk.sh
# DESCRIPTION: Pousse l'image Docker vers Docker Hub (username: Jawk)
# USAGE: ./push-to-dockerhub-jawk.sh
# ============================================================================

echo "╔════════════════════════════════════════════════════════════╗"
echo "║           PUBLICATION SUR DOCKER HUB (JAWK)               ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# -----------------------------------------------------------------
# Variables
# -----------------------------------------------------------------
DOCKERHUB_USERNAME="Jawk"
IMAGE_NAME="consultation-backend"
TAG="latest"
FULL_IMAGE_NAME="${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"
LOCAL_IMAGE="consultation-backend:latest"
DOCKERHUB_URL="https://hub.docker.com/r/${DOCKERHUB_USERNAME}/${IMAGE_NAME}"

# -----------------------------------------------------------------
# Vérifications initiales
# -----------------------------------------------------------------
echo "🔍 Vérifications préliminaires..."
echo "--------------------------------"

# 1. Vérifier que l'image locale existe
if ! docker image inspect $LOCAL_IMAGE > /dev/null 2>&1; then
    echo "❌ ERREUR: Image locale '$LOCAL_IMAGE' non trouvée"
    echo ""
    echo "📋 Solutions:"
    echo "   1. Construisez d'abord l'image: ./build-docker-jawk.sh"
    echo "   2. Vérifiez le nom de l'image: docker images"
    exit 1
fi
echo "✅ Image locale trouvée: $LOCAL_IMAGE"

# 2. Vérifier que l'image est taggée pour Docker Hub
if ! docker image inspect $FULL_IMAGE_NAME > /dev/null 2>&1; then
    echo "⚠️  Image non taggée pour Docker Hub, taggage en cours..."
    if ! docker tag $LOCAL_IMAGE $FULL_IMAGE_NAME; then
        echo "❌ Échec du taggage"
        exit 1
    fi
    echo "✅ Image taggée: $FULL_IMAGE_NAME"
else
    echo "✅ Image déjà taggée pour Docker Hub: $FULL_IMAGE_NAME"
fi

# -----------------------------------------------------------------
# Connexion à Docker Hub
# -----------------------------------------------------------------
echo ""
echo "🔐 CONNEXION À DOCKER HUB"
echo "-------------------------"
echo ""
echo "ℹ️  Vous allez être invité à vous connecter à Docker Hub"
echo "   Utilisez votre username: $DOCKERHUB_USERNAME"
echo ""

# Vérifier si déjà connecté
if ! docker system info | grep -q "Username: $DOCKERHUB_USERNAME"; then
    echo "➡️  Connexion requise..."
    if ! docker login; then
        echo "❌ ÉCHEC DE LA CONNEXION"
        echo ""
        echo "🔧 Dépannage:"
        echo "   1. Vérifiez votre username: $DOCKERHUB_USERNAME"
        echo "   2. Vérifiez votre mot de passe"
        echo "   3. Créez un compte sur: https://hub.docker.com"
        echo "   4. Vérifiez votre connexion internet"
        exit 1
    fi
    echo "✅ Connecté à Docker Hub"
else
    echo "✅ Déjà connecté à Docker Hub en tant que: $DOCKERHUB_USERNAME"
fi

# -----------------------------------------------------------------
# Publication sur Docker Hub
# -----------------------------------------------------------------
echo ""
echo "📤 PUBLICATION EN COURS..."
echo "-------------------------"
echo "🔄 Envoi de: $FULL_IMAGE_NAME"
echo "📡 Vers: $DOCKERHUB_URL"
echo ""
echo "ℹ️  Cette opération peut prendre quelques minutes..."
echo "   La taille de l'image sera affichée ci-dessous:"
echo ""

# Afficher la taille avant le push
IMAGE_SIZE=$(docker image inspect $FULL_IMAGE_NAME --format='{{.Size}}')
IMAGE_SIZE_MB=$((IMAGE_SIZE / 1024 / 1024))
echo "📏 Taille à envoyer: ${IMAGE_SIZE_MB} MB"

# Exécuter le push
START_TIME=$(date +%s)
if docker push $FULL_IMAGE_NAME; then
    END_TIME=$(date +%s)
    DURATION=$((END_TIME - START_TIME))
    
    echo ""
    echo "✅ PUBLICATION RÉUSSIE !"
    echo ""
    echo "📊 STATISTIQUES:"
    echo "   ⏱️  Durée: ${DURATION} secondes"
    echo "   📦 Taille: ${IMAGE_SIZE_MB} MB"
    echo "   📍 Destination: $DOCKERHUB_URL"
else
    echo ""
    echo "❌ ÉCHEC DE LA PUBLICATION"
    echo ""
    echo "🔧 Dépannage:"
    echo "   1. Vérifiez que le repository existe sur Docker Hub"
    echo "      Créez-le sur: https://hub.docker.com/repository/create"
    echo "   2. Vérifiez vos permissions"
    echo "   3. Vérifiez votre connexion internet"
    echo "   4. Réessayez avec: docker push $FULL_IMAGE_NAME"
    exit 1
fi

# -----------------------------------------------------------------
# Vérification post-publication
# -----------------------------------------------------------------
echo ""
echo "🔍 VÉRIFICATION POST-PUBLICATION"
echo "-------------------------------"

echo "📡 Vérification de la présence sur Docker Hub..."
sleep 3  # Attendre que Docker Hub mette à jour

# Vérifier via l'API Docker Hub
if curl -s "https://hub.docker.com/v2/repositories/${DOCKERHUB_USERNAME}/${IMAGE_NAME}/" | grep -q '"name":"'${IMAGE_NAME}'"'; then
    echo "✅ Repository trouvé sur Docker Hub"
    
    # Récupérer les infos
    API_RESPONSE=$(curl -s "https://hub.docker.com/v2/repositories/${DOCKERHUB_USERNAME}/${IMAGE_NAME}/")
    LAST_UPDATED=$(echo "$API_RESPONSE" | grep -o '"last_updated":"[^"]*"' | cut -d'"' -f4)
    
    echo "   🕒 Dernière mise à jour: ${LAST_UPDATED:-Inconnue}"
else
    echo "⚠️  Repository non encore visible (peut prendre quelques minutes)"
fi

# -----------------------------------------------------------------
# Résumé final
# -----------------------------------------------------------------
echo ""
echo "🎉 PUBLICATION TERMINÉE AVEC SUCCÈS !"
echo ""
echo "🔗 LIENS IMPORTANTS:"
echo "   🌐 Page Docker Hub: $DOCKERHUB_URL"
echo "   👤 Votre profil: https://hub.docker.com/u/$DOCKERHUB_USERNAME"
echo ""
echo "🚀 COMMANDES DE TEST:"
echo "   1. Télécharger depuis Docker Hub: docker pull $FULL_IMAGE_NAME"
echo "   2. Exécuter l'image: docker run -p 10001:10001 $FULL_IMAGE_NAME"
echo "   3. Vérifier: ./check-dockerhub-jawk.sh"
echo ""
echo "📋 PROCHAINE ÉTAPE:"
echo "   Déployez sur Render avec l'image: $FULL_IMAGE_NAME"