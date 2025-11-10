# 🔧 GUIA DE COMPILAÇÃO - NPC Battle Mod

## ❌ PROBLEMA DETECTADO
O Gradle não está instalado no seu sistema Windows.

## ✅ SOLUÇÃO - 3 OPÇÕES:

---

## 📦 OPÇÃO 1: Instalar Gradle (Recomendado)

### Passo 1: Baixar Gradle
1. Acesse: https://gradle.org/releases/
2. Baixe: **gradle-8.8-bin.zip**

### Passo 2: Instalar
1. Extraia para: `C:\Gradle\gradle-8.8`
2. Adicione ao PATH do Windows:
   - Abra: Painel de Controle > Sistema > Configurações Avançadas
   - Clique em: "Variáveis de Ambiente"
   - Em "Path", adicione: `C:\Gradle\gradle-8.8\bin`

### Passo 3: Verificar Instalação
```powershell
gradle --version
```

### Passo 4: Compilar o Mod
```powershell
cd "c:\Users\Usuário\Desktop\Nova pasta\npc-battle-mod"
gradle wrapper
.\gradlew build
```

### Passo 5: Pegar o JAR
O arquivo compilado estará em:
```
npc-battle-mod\build\libs\npcbattle-1.0.0.jar
```

---

## 🚀 OPÇÃO 2: Usar IntelliJ IDEA (Mais Fácil)

### Passo 1: Baixar IntelliJ IDEA Community
- Link: https://www.jetbrains.com/idea/download/
- Versão: Community (Grátis)

### Passo 2: Abrir o Projeto
1. Abra IntelliJ IDEA
2. File > Open
3. Selecione a pasta: `npc-battle-mod`
4. Aguarde o IntelliJ baixar dependências automaticamente

### Passo 3: Compilar
1. Clique em: View > Tool Windows > Gradle
2. Expanda: npcbattle > Tasks > build
3. Duplo clique em: **build**

### Passo 4: Pegar o JAR
```
npc-battle-mod\build\libs\npcbattle-1.0.0.jar
```

---

## 🌐 OPÇÃO 3: Usar GitHub Actions (Online)

### Passo 1: Subir para GitHub
```powershell
cd "c:\Users\Usuário\Desktop\Nova pasta\npc-battle-mod"
git remote add origin https://github.com/SEU_USUARIO/npcbattle.git
git push -u origin master
```

### Passo 2: Criar Workflow
Crie: `.github/workflows/build.yml` com o conteúdo abaixo.

### Passo 3: GitHub Compila Automaticamente
O GitHub Actions vai compilar e disponibilizar o JAR para download.

---

## 📝 ARQUIVO: .github/workflows/build.yml

```yaml
name: Build Mod

on:
  push:
    branches: [ master, main ]
  pull_request:
    branches: [ master, main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Build with Gradle
      run: ./gradlew build
      
    - name: Upload JAR
      uses: actions/upload-artifact@v3
      with:
        name: npcbattle-mod
        path: build/libs/*.jar
```

---

## 🎯 REQUISITOS DO SISTEMA

Para compilar localmente você precisa:
- ✅ Java 21 (JDK)
- ✅ Gradle 8.8+
- ✅ Conexão com internet (para baixar dependências)

### Verificar Java:
```powershell
java -version
```

Se não tiver Java 21, baixe em:
- https://adoptium.net/temurin/releases/

---

## 🔍 TROUBLESHOOTING

### Erro: "Java version is too old"
- Instale Java 21 ou superior

### Erro: "Could not resolve dependencies"
- Verifique sua conexão com internet
- Tente: `.\gradlew build --refresh-dependencies`

### Erro: "Permission denied"
- No Linux/Mac: `chmod +x gradlew`
- No Windows: Execute PowerShell como Administrador

---

## 📦 DEPOIS DE COMPILAR

1. Localize o arquivo: `npcbattle-1.0.0.jar`
2. Copie para a pasta `mods` do Minecraft
3. Execute o Minecraft com Fabric Loader
4. Use o comando: `/npcbattle standard`

---

## 💡 RECOMENDAÇÃO

Para desenvolvimento de mods, **IntelliJ IDEA** é a opção mais prática:
- ✅ Gradle integrado
- ✅ Autocomplete do código
- ✅ Debug facilitado
- ✅ Hot reload

---

**Precisa de ajuda?** Escolha uma das 3 opções acima e siga os passos! 🚀
