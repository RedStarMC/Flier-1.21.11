# 集成

Flier 完全独立运行，不需要任何额外插件。你也可以安装以下插件来启用附加功能：

## [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

如果服务器装有 PlaceholderAPI，Flier 会注册 `flier_` 前缀的占位符。
它由点号分隔的多个参数组成，例如 `%flier_first.second.third%`。

### 游戏参数：`game`

该参数显示游戏的各种信息。第二个参数是大厅名，第三个是游戏名，
第四个是游戏的序号（同类型游戏可能同时运行多个）。
第五个参数是你想显示的信息类型：

* `name` 游戏的名称
* `arena` 当前承载该游戏的场地（arena）名称
* `players` 游戏中的玩家数量
* `locked` 游戏是否对新玩家开放（Open / Closed）

**示例**：`%flier_game.main_lobby.the_deathmatch.0.players%` —— 显示
`main_lobby` 大厅中第一个 `the_deathmatch` 游戏的玩家数量。

## 已移除的集成（1.21.11 迁移说明）

以下集成在新版本中已被移除，旧文档中的相关章节不再适用：

### ~~BetonQuest~~（已移除）

旧版本为 BetonQuest 1.x 提供了一组条件和目标（`activator`、`flierengine`、
`ingame`、`inlobby`、`joinlobby`、`joingame`、`flierrespawn`、`flieruse`、
`flierbutton`、`flierkill`、`flierhit`、`flierdeath`、`fliergethit`）。
由于 BetonQuest 2.x 的 API 完全重构，旧集成代码无法在新版本工作，
已从插件中移除。如果你需要任务系统联动（新手教程、附加游戏规则等），
该集成需要按 BetonQuest 2.x API 重写，属于独立的后续工作。

### ~~BountifulAPI~~（已移除）

旧版本用 BountifulAPI 显示标题并在动作栏显示弹药量。该插件最高仅支持
1.10，在 1.21 上不存在；标题与动作栏功能已改用高版本服务端原生 API
实现，无需安装任何额外插件。

## BetonLangAPI【待验证】

旧版本通过 [BetonLangAPI](https://github.com/Co0sh/BetonLangAPI)
按玩家所选语言显示消息。物品名称、Lore、队伍和职业名称可通过
`$something` 标签替换为 _messages.yml_ 中 `something` 键下的实际文本，
实现多语言。注意标签名不要与插件自带消息键冲突。

带翻译职业名的套装示例（_sets.yml_）：

translated_set:
  name: '$className'
  category: main
  [...]

对应的 _messages.yml_：

en:
  className: Fast
  [...]
pl:
  className: Szybki
  [...]

迁移后该挂钩是否保留需要验证；未安装语言插件时将回退到默认语言段。
