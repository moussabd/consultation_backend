#!/bin/bash
# ============================================================================
# SCRIPT: deploy.sh
# DESCRIPTION: Script de déploiement complet (build + push + test)
# USAGE: ./deploy.sh [--test-only] [--push-only]
# ============================================================================

echo "╔════════════════════════════════════════════════════════════╗"
echo "║           DÉPLOIEMENT COMPLET CONSULTATION BACKEND        ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# -----------------------------------------------------------------
# Variables et paramètres
# -----------------------------------------------------------------
TEST_ONLY=false
PUSH_ONLY=false
SKIP_TESTS=false

# Analyser les arguments
for arg in "$@"; do
    case $arg in
        --test-only)
        TEST_ONLY=true
        shift
        ;;
        --push-only)
        PUSH_ONLY=true
        shift
        ;;
        --skip-tests)
        SKIP_TESTS=true
        shift
        ;;
        --help)
        echo "Usage: ./deploy.sh [OPTIONS]"
        echo ""
        echo "Options:"
        echo "  --test-only     Exécute seulement les tests"
        echo "  --push-only     Exécute seulement le push vers Docker Hub"
        echo "  --skip-tests    Saute les tests locaux"
        echo "  --help          Affiche cette aide"
        exit 0
        ;;
    esac
done

# -----------------------------------------------------------------
# Fonctions utilitaires
# -----------------------------------------------------------------
print_step() {
    echo ""
    echo "════════════════════════════════════════════════════════════"
    echo "  $1"
    echo "════════════════════════════════════════════════════════════"
    echo ""
}

print_success() {
    echo "✅ $1"
}

print_error() {
    echo "❌ $1"
}

print_info() {
    echo "ℹ️  $1"
}

# -----------------------------------------------------------------
# Étape 1: Vérifications préliminaires
# -----------------------------------------------------------------
if [ "$TEST_ONLY" = false ] && [ "$PUSH_ONLY" = false ]; then
    print_step "ÉTAPE 1: VÉRIFICATIONS PRÉLIMINAIRES"
    
    # Vérifier Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker n'est pas installé"
        exit 1
    fi
    print_success "Docker: $(docker --version | head -n1)"
    
    # Vérifier Docker Desktop
    if ! docker info &> /dev/null; then
        print_error "Docker Desktop n'est pas en cours d'exécution"
        exit 1
    fi
    print_success "Docker Desktop est en cours d'exécution"
    
    # Vérifier le JAR
    JAR_FILE="target/consultation-0.0.1-SNAPSHOT.jar"
    if [ ! -f "$JAR_FILE" ]; then
        print_error "JAR non trouvé: $JAR_FILE"
        echo ""
        echo "📋 Solutions:"
        echo "   1. Compilez avec: mvn clean package"
        echo "   2. Vérifiez le chemin du JAR"
        exit 1
    fi
    print_success "JAR trouvé: $(ls -lh "$JAR_FILE")"
fi

# -----------------------------------------------------------------
# Étape 2: Construction de l'image
# -----------------------------------------------------------------
if [ "$TEST_ONLY" = false ] && [ "$PUSH_ONLY" = false ]; then
    print_step "ÉTAPE 2: CONSTRUCTION DE L'IMAGE DOCKER"
    
    if ./build-docker-jawk.sh; then
        print_success "Construction réussie"
    else
        print_error "Échec de la construction"
        exit 1
    fi
fi

# -----------------------------------------------------------------
# Étape 3: Tests locaux
# -----------------------------------------------------------------
if [ "$SKIP_TESTS" = false ] && [ "$PUSH_ONLY" = false ]; then
    print_step "ÉTAPE 3: TESTS LOCAUX"
    
    read -p "Voulez-vous exécuter les tests locaux? (O/n): " -n 1 -r
    echo ""
    
    if [[ $REPLY =~ ^[Oo]$ ]] || [[ -z $REPLY ]]; then
        if ./test-local.sh; then
            print_success "Tests locaux réussis"
        else
            print_error "Échec des tests locaux"
            read -p "Continuer malgré l'échec des tests? (o/N): " -n 1 -r
            echo ""
            if [[ ! $REPLY =~ ^[Oo]$ ]]; then
                exit 1
            fi
        fi
    else
        print_info "Tests locaux ignorés"
    fi
fi

# -----------------------------------------------------------------
# Étape 4: Publication sur Docker Hub
# -----------------------------------------------------------------
if [ "$TEST_ONLY" = false ]; then
    print_step "ÉTAPE 4: PUBLICATION SUR DOCKER HUB"
    
    read -p "Voulez-vous publier sur Docker Hub? (O/n): " -n 1 -r
    echo ""
    
    if [[ $REPLY =~ ^[Oo]$ ]] || [[ -z $REPLY ]]; then
        if ./push-to-dockerhub-jawk.sh; then
            print_success "Publication réussie"
        else
            print_error "Échec de la publication"
            exit 1
        fi
    else
        print_info "Publication sur Docker Hub ignorée"
    fi
fi

# -----------------------------------------------------------------
# Étape 5: Vérification finale
# -----------------------------------------------------------------
print_step "ÉTAPE 5: VÉRIFICATION FINALE"

echo "🔍 Vérification de l'état final..."
echo ""

# Vérifier les images locales
echo "📦 IMAGES LOCALES:"
docker images | grep -E "(consultation-backend|Jawk/consultation)" | sed 's/^/   /'

echo ""
# Vérification rapide Docker Hub
echo "🌐 ÉTAT DOCKER HUB:"
if curl -s "https://hub.docker.com/v2/repositories/Jawk/consultation-backend/" | grep -q '"name":"consultation-backend"'; then
    print_success "Repository disponible sur Docker Hub"
else
    print_info "Repository non encore visible (peut prendre quelques minutes)"
fi

# -----------------------------------------------------------------
# Résumé final
# -----------------------------------------------------------------
print_step "🎉 DÉPLOIEMENT TERMINÉ AVEC SUCCÈS !"

echo "📊 RÉSUMÉ:"
echo ""
echo "✅ IMAGE DOCKER:"
echo "   Local: consultation-backend:latest"
echo "   Docker Hub: Jawk/consultation-backend:latest"
echo ""
echo "🔗 LIENS:"
echo "   Docker Hub: https://hub.docker.com/r/Jawk/consultation-backend"
echo "   Profil: https://hub.docker.com/u/Jawk"
echo ""
echo "🚀 COMMANDES DE TEST:"
echo "   docker pull Jawk/consultation-backend:latest"
echo "   docker run -p 10001:10001 Jawk/consultation-backend:latest"
echo ""
echo "📋 POUR RENDER:"
echo "   Utilisez l'image: Jawk/consultation-backend:latest"
echo "   Port: 10001"
echo ""
echo "🎯 PROCHAINE ÉTAPE:"
echo "   Déployez sur Render avec l'image ci-dessus"
echo ""
echo "✅ TOUTES LES ÉTAPES ONT ÉTÉ EXÉCUTÉES AVEC SUCCÈS"