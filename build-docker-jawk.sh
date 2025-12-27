#!/bin/bash
# ============================================================================
# SCRIPT: build-docker-jawk.sh
# DESCRIPTION: Construit l'image Docker et la tagge pour Jawk
# USAGE: ./build-docker-jawk.sh
# ============================================================================

echo "╔════════════════════════════════════════════════════════════╗"
echo "║           CONSTRUCTION DOCKER POUR JAWK                    ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# -----------------------------------------------------------------
# Variables
# -----------------------------------------------------------------
LOCAL_IMAGE="consultation-backend:latest"
DOCKERHUB_IMAGE="Jawk/consultation-backend:latest"
JAR_FILE="target/consultation-0.0.1-SNAPSHOT.jar"

# -----------------------------------------------------------------
# Vérifications initiales
# -----------------------------------------------------------------
echo "🔍 Vérifications préliminaires..."
echo "--------------------------------"

# 1. Vérifier Docker
if ! command -v docker &> /dev/null; then
    echo "❌ ERREUR: Docker n'est pas installé ou n'est pas dans le PATH"
    echo "   Installez Docker Desktop depuis: https://www.docker.com/products/docker-desktop/"
    exit 1
fi
echo "✅ Docker est installé: $(docker --version | head -n1)"

# 2. Vérifier que Docker Desktop est en cours d'exécution
if ! docker info &> /dev/null; then
    echo "❌ ERREUR: Docker Desktop n'est pas en cours d'exécution"
    echo "   Démarrez Docker Desktop et réessayez"
    exit 1
fi
echo "✅ Docker Desktop est en cours d'exécution"

# 3. Vérifier le fichier JAR
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ ERREUR: Fichier JAR non trouvé: $JAR_FILE"
    echo ""
    echo "📂 Contenu du dossier courant:"
    ls -la
    echo ""
    echo "📂 Contenu du dossier target/:"
    ls -la target/ 2>/dev/null || echo "Dossier 'target/' non trouvé"
    echo ""
    echo "🔧 Solutions possibles:"
    echo "   1. Compilez le projet: mvn clean package"
    echo "   2. Vérifiez le nom du fichier JAR dans target/"
    echo "   3. Assurez-vous d'être dans le bon répertoire"
    exit 1
fi
echo "✅ JAR trouvé: $(ls -lh "$JAR_FILE")"

# -----------------------------------------------------------------
# Construction de l'image
# -----------------------------------------------------------------
echo ""
echo "🔨 Construction de l'image Docker..."
echo "-----------------------------------"

# Supprimer l'image existante si elle existe
echo "🧹 Nettoyage des images existantes..."
docker rmi -f $LOCAL_IMAGE 2>/dev/null
docker rmi -f $DOCKERHUB_IMAGE 2>/dev/null

# Construire la nouvelle image
echo "🏗️  Construction en cours..."
if docker build -t $LOCAL_IMAGE .; then
    echo ""
    echo "✅ CONSTRUCTION RÉUSSIE"
else
    echo ""
    echo "❌ ÉCHEC DE LA CONSTRUCTION"
    echo "   Vérifiez votre Dockerfile et réessayez"
    exit 1
fi

# -----------------------------------------------------------------
# Taggage pour Docker Hub
# -----------------------------------------------------------------
echo ""
echo "🏷️  Taggage de l'image pour Docker Hub..."
echo "----------------------------------------"

if docker tag $LOCAL_IMAGE $DOCKERHUB_IMAGE; then
    echo "✅ Image taggée: $DOCKERHUB_IMAGE"
else
    echo "❌ Échec du taggage"
    exit 1
fi

# -----------------------------------------------------------------
# Résumé final
# -----------------------------------------------------------------
echo ""
echo "📊 RÉSUMÉ DE LA CONSTRUCTION"
echo "============================="
echo ""
echo "📦 IMAGES CRÉÉES:"
echo "   🔹 $LOCAL_IMAGE"
echo "   🔹 $DOCKERHUB_IMAGE"
echo ""
echo "📐 TAILLE DE L'IMAGE:"
docker images $LOCAL_IMAGE --format "   📏 Taille: {{.Size}}" | head -1
echo ""
echo "🚀 COMMANDES SUIVANTES:"
echo "   1. Tester localement: ./test-local.sh"
echo "   2. Pousser vers Docker Hub: ./push-to-dockerhub-jawk.sh"
echo "   3. Vérifier sur Docker Hub: ./check-dockerhub-jawk.sh"
echo ""
echo "🔍 VÉRIFICATION RAPIDE:"
docker images | grep -E "(consultation-backend|Jawk/consultation)" | sed 's/^/   /'
echo ""
echo "✅ CONSTRUCTION TERMINÉE AVEC SUCCÈS !"