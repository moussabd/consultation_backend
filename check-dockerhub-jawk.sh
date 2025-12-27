#!/bin/bash
# ============================================================================
# SCRIPT: check-dockerhub-jawk.sh
# DESCRIPTION: Vérifie l'état de l'image sur Docker Hub pour Jawk
# USAGE: ./check-dockerhub-jawk.sh
# ============================================================================

echo "╔════════════════════════════════════════════════════════════╗"
echo "║           VÉRIFICATION DOCKER HUB (JAWK)                   ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# -----------------------------------------------------------------
# Variables
# -----------------------------------------------------------------
DOCKERHUB_USERNAME="Jawk"
IMAGE_NAME="consultation-backend"
TAG="latest"
FULL_IMAGE_NAME="${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"
DOCKERHUB_URL="https://hub.docker.com/r/${DOCKERHUB_USERNAME}/${IMAGE_NAME}"
API_BASE_URL="https://hub.docker.com/v2/repositories/${DOCKERHUB_USERNAME}/${IMAGE_NAME}"

# -----------------------------------------------------------------
# Section 1: Vérification du Repository
# -----------------------------------------------------------------
echo "📦 SECTION 1: VÉRIFICATION DU REPOSITORY"
echo "========================================"
echo ""

echo "🔗 URL du repository: $DOCKERHUB_URL"
echo "📡 Interrogation de l'API Docker Hub..."
echo ""

# Récupérer les informations du repository
REPO_RESPONSE=$(curl -s "$API_BASE_URL/")

if echo "$REPO_RESPONSE" | grep -q '"detail":"Not found"'; then
    echo "❌ REPOSITORY NON TROUVÉ"
    echo ""
    echo "📋 Causes possibles:"
    echo "   1. Le repository n'existe pas encore"
    echo "   2. Le repository est privé et vous n'y avez pas accès"
    echo "   3. Mauvais username ou nom d'image"
    echo ""
    echo "🔧 Solution:"
    echo "   Créez le repository sur: https://hub.docker.com/repository/create"
    echo "   Nom: $IMAGE_NAME"
    echo "   Visibilité: Public (recommandé)"
    exit 1
fi

# Extraire les informations
REPO_NAME=$(echo "$REPO_RESPONSE" | grep -o '"name":"[^"]*"' | cut -d'"' -f4)
DESCRIPTION=$(echo "$REPO_RESPONSE" | grep -o '"description":"[^"]*"' | cut -d'"' -f4)
IS_PRIVATE=$(echo "$REPO_RESPONSE" | grep -o '"is_private":[a-z]*' | cut -d':' -f2)
STAR_COUNT=$(echo "$REPO_RESPONSE" | grep -o '"star_count":[0-9]*' | cut -d':' -f2)
PULL_COUNT=$(echo "$REPO_RESPONSE" | grep -o '"pull_count":[0-9]*' | cut -d':' -f2)
LAST_UPDATED=$(echo "$REPO_RESPONSE" | grep -o '"last_updated":"[^"]*"' | cut -d'"' -f4)

echo "✅ REPOSITORY TROUVÉ"
echo ""
echo "📊 INFORMATIONS DU REPOSITORY:"
echo "   👤 Propriétaire: $DOCKERHUB_USERNAME"
echo "   📦 Nom: $REPO_NAME"
echo "   📝 Description: ${DESCRIPTION:-Aucune}"
echo "   🔒 Visibilité: $( [ "$IS_PRIVATE" = "true" ] && echo "Privé" || echo "Public" )"
echo "   ⭐ Étoiles: ${STAR_COUNT:-0}"
echo "   📥 Téléchargements: ${PULL_COUNT:-0}"
echo "   🕒 Dernière mise à jour: ${LAST_UPDATED:-Inconnue}"
echo ""

# -----------------------------------------------------------------
# Section 2: Vérification des Tags
# -----------------------------------------------------------------
echo "🏷️  SECTION 2: VÉRIFICATION DES TAGS"
echo "===================================="
echo ""

echo "📡 Récupération des tags..."
TAGS_RESPONSE=$(curl -s "${API_BASE_URL}/tags/")

if echo "$TAGS_RESPONSE" | grep -q '"count":0'; then
    echo "❌ AUCUN TAG TROUVÉ"
    echo ""
    echo "ℹ️  Le repository existe mais ne contient pas encore d'image"
    echo "   Poussez une image avec: ./push-to-dockerhub-jawk.sh"
else
    TAG_COUNT=$(echo "$TAGS_RESPONSE" | grep -o '"count":[0-9]*' | cut -d':' -f2)
    echo "✅ Nombre de tags: $TAG_COUNT"
    echo ""
    echo "📋 LISTE DES TAGS:"
    echo "------------------"
    
    # Extraire et afficher les tags
    echo "$TAGS_RESPONSE" | grep -o '"name":"[^"]*"' | cut -d'"' -f4 | while read TAG_NAME; do
        # Récupérer les infos spécifiques à chaque tag
        TAG_INFO=$(curl -s "${API_BASE_URL}/tags/${TAG_NAME}/")
        TAG_SIZE=$(echo "$TAG_INFO" | grep -o '"full_size":[0-9]*' | cut -d':' -f2)
        TAG_LAST_UPDATED=$(echo "$TAG_INFO" | grep -o '"last_updated":"[^"]*"' | cut -d'"' -f4)
        
        if [ -n "$TAG_SIZE" ]; then
            SIZE_MB=$((TAG_SIZE / 1024 / 1024))
            SIZE_STR="${SIZE_MB} MB"
        else
            SIZE_STR="Inconnue"
        fi
        
        if [ "$TAG_NAME" = "$TAG" ]; then
            echo "   ✅ $TAG_NAME (dernier: ${TAG_LAST_UPDATED:-?}, taille: $SIZE_STR) ← ACTUEL"
        else
            echo "   • $TAG_NAME (dernier: ${TAG_LAST_UPDATED:-?}, taille: $SIZE_STR)"
        fi
    done
fi

# -----------------------------------------------------------------
# Section 3: Test de téléchargement
# -----------------------------------------------------------------
echo ""
echo "⬇️  SECTION 3: TEST DE TÉLÉCHARGEMENT"
echo "===================================="
echo ""

echo "🔄 Tentative de téléchargement: $FULL_IMAGE_NAME"
echo ""

# Vérifier si l'image existe localement
if docker image inspect $FULL_IMAGE_NAME > /dev/null 2>&1; then
    echo "ℹ️  Image déjà présente localement"
    LOCAL_SIZE=$(docker image inspect $FULL_IMAGE_NAME --format='{{.Size}}')
    LOCAL_SIZE_MB=$((LOCAL_SIZE / 1024 / 1024))
    echo "   📏 Taille locale: ${LOCAL_SIZE_MB} MB"
    
    read -p "   Voulez-vous retélécharger? (o/N): " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Oo]$ ]]; then
        echo "   ⏩ Téléchargement ignoré"
    else
        docker pull $FULL_IMAGE_NAME
    fi
else
    echo "🔄 Téléchargement en cours..."
    if docker pull $FULL_IMAGE_NAME; then
        echo "✅ TÉLÉCHARGEMENT RÉUSSI"
        
        # Afficher les détails
        echo ""
        echo "📊 DÉTAILS DE L'IMAGE TÉLÉCHARGÉE:"
        docker image inspect $FULL_IMAGE_NAME --format='\
        👤 User: {{.Config.User}}\n\
        📏 Taille: {{.Size}} bytes\n\
        🏗️  Créée: {{.Created}}\n\
        💻 OS: {{.Os}} / {{.Architecture}}\n\
        🔗 Cmd: {{.Config.Cmd}}' | sed 's/^/   /'
    else
        echo "❌ ÉCHEC DU TÉLÉCHARGEMENT"
        echo "   Vérifiez:"
        echo "   1. Que le repository existe"
        echo "   2. Que le tag existe"
        echo "   3. Votre connexion internet"
    fi
fi

# -----------------------------------------------------------------
# Section 4: Test d'exécution rapide
# -----------------------------------------------------------------
echo ""
echo "🚀 SECTION 4: TEST D'EXÉCUTION"
echo "=============================="
echo ""

read -p "Voulez-vous tester l'exécution de l'image? (o/N): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Oo]$ ]]; then
    echo "🧪 Test d'exécution rapide..."
    echo ""
    
    # Arrêter tout conteneur existant
    docker stop dockerhub-test 2>/dev/null
    docker rm dockerhub-test 2>/dev/null
    
    # Démarrer un conteneur test
    echo "▶️  Démarrage du conteneur test..."
    docker run -d \
        --name dockerhub-test \
        -p 10002:10001 \
        -e PORT=10001 \
        $FULL_IMAGE_NAME
    
    sleep 5
    
    echo ""
    echo "📝 LOGS DU CONTENEUR:"
    echo "-------------------"
    docker logs dockerhub-test --tail 10
    
    echo ""
    echo "🔍 ÉTAT DU CONTENEUR:"
    docker ps --filter "name=dockerhub-test" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    
    echo ""
    echo "🧹 Nettoyage..."
    docker stop dockerhub-test
    docker rm dockerhub-test
    echo "✅ Test terminé"
fi

# -----------------------------------------------------------------
# Résumé final
# -----------------------------------------------------------------
echo ""
echo "🎉 VÉRIFICATION TERMINÉE"
echo "========================"
echo ""
echo "🔗 LIENS:"
echo "   🌐 Page web: $DOCKERHUB_URL"
echo "   👤 Profil: https://hub.docker.com/u/$DOCKERHUB_USERNAME"
echo ""
echo "✅ STATUT: Repository $( [ -n "$REPO_NAME" ] && echo "EXISTE" || echo "NON TROUVÉ" )"
echo "✅ TAGS: $( [ "$TAG_COUNT" -gt 0 ] && echo "$TAG_COUNT trouvé(s)" || echo "Aucun" )"
echo ""
echo "📋 COMMANDES UTILES:"
echo "   docker pull $FULL_IMAGE_NAME"
echo "   docker run -p 10001:10001 $FULL_IMAGE_NAME"
echo "   docker images | grep Jawk"