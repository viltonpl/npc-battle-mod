# NPC Battle Command Mod

Um mod Fabric que adiciona o comando `/npcbattle` para batalhar com NPCs treinadores do Cobblemon.

## 🎮 Funcionalidades

- ⚔️ Comando `/npcbattle <nome>` nativo do Minecraft
- 🔍 Autocompletar com TAB
- 🎨 Mensagens coloridas e formatadas
- ✅ Verifica se o jogador tem Pokémon
- 🔎 Busca NPCs em raio de 50 blocos
- 🎯 Funciona em blocos de comando

## 📦 Instalação

### Para Jogadores
1. Baixe o arquivo `.jar` da [página de releases](../../releases)
2. Coloque na pasta `mods` do seu Minecraft
3. Certifique-se de ter Fabric API e Cobblemon instalados

### Requisitos
- Minecraft 1.21.1
- Fabric Loader 0.16.5+
- Fabric API 0.116.1+
- Cobblemon 1.6.1+

## 🎯 Como Usar

### Comando Básico
```
/npcbattle <nome_do_treinador>
```

### Exemplos
```
/npcbattle standard
/npcbattle sacchi
/npcbattle ai_test
```

### Treinadores Disponíveis
- **standard** - Treinador Padrão (6 Pokémon nível 100)
- **sacchi** - Professora Sacchi
- **ai_test** - Treinador de Teste

### Em Blocos de Comando
```
/execute as @p run npcbattle standard
```

## 🔧 Para Desenvolvedores

### Configurar Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/seunome/npcbattle.git
cd npcbattle
```

2. Configure o workspace:
```bash
./gradlew genSources
```

3. Compile o mod:
```bash
./gradlew build
```

4. O arquivo `.jar` estará em `build/libs/`

### Estrutura do Projeto

```
npc-battle-mod/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/yourname/npcbattle/
│       │       ├── NpcBattleCommandMod.java
│       │       └── commands/
│       │           └── BattleCommand.java
│       └── resources/
│           └── fabric.mod.json
├── build.gradle
├── gradle.properties
└── README.md
```

### Adicionar Mais Treinadores

1. Abra `BattleCommand.java`
2. Adicione o nome na lista `AVAILABLE_NPCS`:
```java
private static final List<String> AVAILABLE_NPCS = List.of(
    "standard",
    "sacchi",
    "ai_test",
    "seu_novo_treinador"  // Adicione aqui
);
```
3. Recompile o mod

## 📝 Licença

MIT License - Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuindo

Contribuições são bem-vindas! Sinta-se livre para:
- Reportar bugs
- Sugerir novas funcionalidades
- Enviar pull requests

## ⚠️ Notas Importantes

- Este mod **requer** que NPCs do Cobblemon existam no mundo
- Use o **NPC Editor** do Cobblemon para criar NPCs antes de usar o comando
- O comando procura NPCs em um raio de 50 blocos do jogador

## 📞 Suporte

- 🐛 Reportar bugs: [GitHub Issues](../../issues)
- 💬 Discord: [Seu servidor Discord]
- 📧 Email: seuemail@exemplo.com

## 🎉 Agradecimentos

- Time do Cobblemon pelo excelente mod
- Comunidade Fabric por toda a documentação e suporte

---

**Desenvolvido com ❤️ para a comunidade Cobblemon**
