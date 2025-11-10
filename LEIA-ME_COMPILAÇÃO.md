# ⚠️ COMPILAÇÃO FALHOU

## 🐛 Problema Detectado
- Incompatibilidade entre versão do Java e Gradle
- É necessário ajustar as configurações manualmente

## ✅ SOLUÇÃO MAIS RÁPIDA: Usar IntelliJ IDEA

### 📥 PASSO 1: Baixar IntelliJ IDEA Community (GRÁTIS)
🔗 **Link**: https://www.jetbrains.com/idea/download/

- Escolha: **Community Edition** (gratuita)
- Sistema: Windows
- Clique em: Download

### 📂 PASSO 2: Abrir o Projeto

1. Abra **IntelliJ IDEA**
2. Na tela inicial, clique: **Open**
3. Navegue até: `C:\Users\Usuário\Desktop\Nova pasta\npc-battle-mod`
4. Selecione a pasta e clique: **OK**

### ⏳ PASSO 3: Aguardar Importação

- O IntelliJ vai **detectar automaticamente** que é um projeto Gradle
- Vai aparecer uma janela: "Trust Gradle Project?" → Clique **Trust Project**
- Aguarde o IntelliJ **baixar todas as dependências** (pode demorar 5-10 minutos)
- Você verá o progresso na barra inferior

### 🔨 PASSO 4: Compilar o Mod

**Opção A - Via Interface:**
1. No lado direito, clique no ícone **Gradle** (elefante)
2. Expanda: `npcbattle` → `Tasks` → `build`
3. **Duplo clique** em: `build`
4. Aguarde a compilação (aparece no painel inferior)

**Opção B - Via Terminal integrado:**
1. No IntelliJ, pressione: `Alt + F12` (abre terminal)
2. Digite: `.\gradlew build`
3. Pressione Enter

### 📦 PASSO 5: Localizar o JAR Compilado

Após a compilação bem-sucedida:
```
📁 npc-battle-mod
  └── 📁 build
      └── 📁 libs
          └── 📄 npcbattle-1.0.0.jar  ← ESTE É O MOD!
```

### 🎮 PASSO 6: Instalar no Minecraft

1. Copie `npcbattle-1.0.0.jar`
2. Cole na pasta `mods` do Minecraft:
   ```
   C:\Users\Usuário\AppData\Roaming\.minecraft\mods\
   ```
3. Certifique-se de ter:
   - Fabric Loader instalado
   - Fabric API na pasta mods
   - Cobblemon instalado

### ✨ PASSO 7: Testar!

1. Abra o Minecraft
2. Entre em um mundo
3. Digite: `/npcbattle standard`
4. Batalhe! ⚔️

---

## 🎯 ALTERNATIVA: Usar Eclipse

Se preferir Eclipse:
1. Baixe: https://www.eclipse.org/downloads/
2. Instale o plugin **Buildship Gradle**
3. File → Import → Gradle → Existing Gradle Project
4. Selecione a pasta `npc-battle-mod`
5. Botão direito no projeto → Gradle → Refresh Gradle Project
6. Botão direito no projeto → Run As → Gradle Build
7. Em Goals, digite: `build`

---

## 🔧 ALTERNATIVA: Corrigir Gradle Manualmente

Se quiser insistir em compilar via linha de comando:

### 1. Baixar Gradle 8.3 (compatível com Java 21)
```powershell
Invoke-WebRequest -Uri "https://services.gradle.org/distributions/gradle-8.3-bin.zip" -OutFile "gradle-8.3.zip"
Expand-Archive -Path "gradle-8.3.zip" -DestinationPath "." -Force
.\gradle-8.3\bin\gradle.bat wrapper
.\gradlew build
```

---

## 💡 RECOMENDAÇÃO FINAL

**Use IntelliJ IDEA** - É a forma mais fácil e profissional:
- ✅ Tudo configurado automaticamente
- ✅ Autocomplete inteligente
- ✅ Debug integrado
- ✅ Git integrado
- ✅ Terminal embutido
- ✅ GRÁTIS!

---

## ❓ PRECISA DE AJUDA?

Se tiver problemas:
1. Certifique-se de ter **Java 21** instalado
2. Use **IntelliJ IDEA** (mais simples)
3. Aguarde o download de dependências (primeira vez demora)
4. Procure por erros no painel "Build" do IntelliJ

---

**Status Atual:**
- ✅ Código do mod: Pronto
- ✅ Java 21: Instalado
- ❌ Gradle: Configuração manual necessária
- 🎯 Solução: **IntelliJ IDEA Community**

Boa sorte! 🚀
