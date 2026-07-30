# 效果（Effect）

效果是游戏中**不改变游戏状态**的行为。"不改变游戏状态"意味着它们不会
真正改变玩家本身——效果不会修复翅膀、不会消耗燃料、也不会攻击其他玩家。
取而代之的是：播放音效、显示标题、发射烟花。效果被绑定到特定的
**事件**上——这样你可以控制它们何时触发。事件和效果都有多种类型，
组合时只有一条规则：**非玩家事件不能触发玩家效果**（非玩家事件的例子
是"游戏开始"，玩家效果的例子是"给单个玩家发送标题"——这两者不兼容，
因为游戏开始不关联任何玩家，而发送标题必须有一个玩家接收）。

效果定义在 _effects.yml_ 文件中，并在 _games.yml_ 的 `effects` 节
挂载到游戏上。每个效果有一个效果类型（可用类型见下文）和一个事件类型
（同样见下文）。你还可以添加所谓的"匹配器"（matcher），进一步缩小
触发效果的事件范围（例如你只想在玩家**使用武器**时播放音效——匹配器
可以匹配 `use` 事件中 `usage` 为武器的条目）。

effect_name:
  type: [效果类型]
  event_type: [事件类型]
  matchers:
    名称: [值]
  [效果专属设置]

要把效果匹配到特定事件，需要定义匹配器。每个匹配器有一个键和一个匹配
条件。键是事件中某个值的名称（在下方 _事件类型_ 一节有描述），匹配
条件定义哪些值可以被接受。共有 5 种匹配方式：

* 文本 —— 值与指定文本相同时匹配
* 文本列表 —— 值在列表中时匹配
* 数字 —— 值等于该数字时匹配
* 数字范围 —— 值是该范围内的数字时匹配
* 布尔值 —— 值相同时匹配

每种各举一例：

matchers:
  text: something
  list:
  - first
  - second
  number: 10
  range: "&lt;(10), &gt;(20)"
  boolean: false

除了范围之外都一目了然。范围有特殊语法（`&lt;(10)`）：第一个字符是
"小于"或"大于"符号，后面是括号中的数字。它会匹配所有小于或大于指定
数字的数。你可以像示例那样用逗号组合两种边界，也可以只指定一个边界。

## 效果设置

* `type`（**必填**）效果的类型。
* `event_type`（**必填**）触发此效果的事件类型。
* `matchers`（**可选**）事件必须满足才能触发效果的匹配器列表。

## 效果类型

### 音效效果（Sound effect）

所有音效效果都播放声音，只是目标不同。以下设置在所有音效效果中通用：

* `sound`（**必填**）[音效类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/Sound.html)。
* `volume`（**默认：1**）音量。
* `pitch`（**默认：1**）音高。

effect_name:
  type: [类型]
  [事件设置]
  sound: [音效类型]
  volume: [正小数]
  pitch: [正小数]

#### 类型：

**`privateSound`**（玩家效果）

只对该玩家播放音效。

**`publicSound`**（玩家效果）

在该玩家的位置对所有能听到的人播放音效。

**`gameSound`**（玩家效果）

对游戏中的每名玩家，在各自的位置播放音效。

### 粒子效果（Particle effect）

**`particle`**

在玩家位置生成粒子。Minecraft 的粒子处理起来普遍比较怪。

* `particle`（**必填**）[粒子类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/Particle.html)。
* `amount`（**默认：0**）粒子数量。设为 0 启用替代粒子模式。
  高数值不会拖垮服务端（客户端是另一回事）。
* `offset`（**默认：0**）粒子随机生成的范围。为 0 时粒子精确出现在
  玩家位置。在替代模式下可能有不同含义。
* `offset_x`、`offset_y`、`offset_z`（**默认：0**）覆盖特定轴的偏移，
  在替代模式下有用，例如设置颜色。
* `speed`（**默认：0**）粒子速度。在替代模式下可能有不同含义。
* `count`（**默认：1**）控制服务端生成粒子的次数。与 `amount` 不同——
  此设置会在服务端侧生成多份粒子，意味着如果每刻生成非常高的数值
  （比如几百个）会拖垮服务端。当你需要替代 `amount` 模式但仍想生成
  多份粒子时使用它。
* `manual_offset`（**默认：0**）与 offset 相同，但用于上述服务端侧生成。
* `manual_offset_x`、`manual_offset_y`、`manual_offset_z`（**默认：0**）
  服务端侧生成中覆盖特定轴的偏移，这次没有特殊含义。

effect_name:
  type: particle
  [事件设置]
  particle: [粒子类型]
  amount: [非负整数]
  offset: [非负小数]
  speed: [非负小数]
  count: [非负整数]
  manual_offset: [非负小数]

### 发光效果（Glow effect）

**`glow`**

使玩家发光一段时间。

shiny:
  type: glow
  [事件设置]
  time: [正整数]

* `time`（**必填**）发光时长（刻）。

## 事件类型

每个玩家事件都有以下可用匹配器：

* `class`（文本）玩家的职业名称。
* `color`（文本）玩家使用的颜色（例如队伍颜色）。
* `money`（数字）玩家拥有的金钱数。
* `engine`（文本）玩家引擎的 ID。
* `fuel`（数字）引擎中的燃料量。
* `fuel_ratio`（数字）燃料/最大燃料的比值，0 到 1 之间。
* `wings`（文本）玩家翅膀的 ID。
* `wings_health`（数字）翅膀的耐久量。
* `wings_health_ratio`（数字）翅膀耐久/最大耐久的比值，0 到 1 之间。

如果事件涉及另一名玩家（比如 `hit` 事件——射击者是主玩家，
受害者是另一名玩家），这些匹配器对另一名玩家也可用，
并带有事件类型指定的前缀。另外还有一个特殊匹配器：

* `attitude`（文本）两名玩家之间的关系。可选值：`friendly`（友方）、
  `neutral`（中立）、`hostile`（敌对）。

非玩家事件没有任何附加匹配器。

### 使用事件（Use event）

**`use`**（玩家事件）

玩家使用带 usage 的物品时触发。

* `item`（文本）此事件中使用的物品 ID。
* `ammo`（数字）使用此物品**之前**的弹药量。
* `amount`（数字）玩家持有的该物品数量。
* `usage`（文本）此事件中使用的 usage 的 ID。

### 命中事件（Hit event）

**`hit`**（双人事件，第二名玩家带 `target_` 前缀）

一名玩家用武器命中另一名玩家时触发。效果为射击者触发。被命中玩家的
匹配器带 `target_` 前缀，例如 `target_class`。

* `self_hit`（true/false）玩家是否命中了自己。

### 被命中事件（Get hit event）

**`get hit`**（双人事件，第二名玩家带 `shooter_` 前缀）

一名玩家被另一名玩家用武器命中时触发。效果为受害者触发。开火玩家的
匹配器带 `shooter_` 前缀，例如 `shooter_money`。

`self_hit` 匹配器与 **hit** 事件相同。

### 击杀事件（Kill event）

**`kill`**（双人事件，第二名玩家带 `killed_` 前缀）

主玩家击杀另一名玩家时触发。效果为击杀者触发。被击杀玩家的匹配器带
`killed_` 前缀，例如 `killed_engine`。自杀时击杀者与被击杀者是
同一名玩家。

* `suicide`（true/false）此事件是否为自杀触发。
* `shot_down`（true/false）玩家是否因从空中坠落而死。
* `killed`（true/false）玩家是否在地面上被击杀（而非坠落）。

### 被击杀事件（Killed event）

**`killed`**（双人事件，第二名玩家带 `killer_` 前缀）

主玩家被另一名玩家击杀时触发。效果为被击杀者触发。击杀者的匹配器带
`killer_` 前缀，例如 `killer_wings`。自杀时击杀者与被击杀者是
同一名玩家。

其余匹配器如 `suicide`、`shot_down` 与上面 `kill` 事件完全相同。

### 引擎使用事件（Engine use event）

**`engine`**（玩家事件）

玩家使用引擎期间每刻触发。没有特殊匹配器。

### 出生事件（Spawn event）

**`spawn`**（玩家事件）

玩家在游戏中出生时触发。没有特殊匹配器。

### 增益道具拾取事件（Bonus collect event）

**`bonus`**（玩家事件）

玩家拾取增益道具时触发。

* `bonus`（文本）增益道具的 ID，定义在 _bonuses.yml_ 中。

### 按钮点击事件（Button click event）

**`button`**（玩家事件）

玩家点击游戏中的按钮时触发。

* `button`（文本）按钮的 ID，定义在 _games.yml_ 中。

### 弹射物发射事件（Projectile launch event）

**`projectile`**（玩家事件）

玩家每次发射弹射物时触发。包括粒子枪械和弹射物枪械——其他攻击只发射
单个弹射物（对它们可以用 `use` 事件）。

* `attack`（文本）发射该弹射物的攻击动作的 ID，定义在 _actions.yml_ 中。

## 迁移提示：音效名与粒子名

本章的 `sound` 和 `particle` 是 1.13 扁平化改名**最多**的两类值，
旧配置几乎必然需要逐条更新。加载失败时控制台会报
"Sound/Particle does not exist" 并指出配置节。

**常见音效名映射**（1.12 → 1.21）：

| 1.12 | 1.21 |
|---|---|
| `ENDERDRAGON_GROWL` | `ENTITY_ENDER_DRAGON_GROWL` |
| `FIREWORK_BLAST` | `ENTITY_FIREWORK_ROCKET_BLAST` |
| `FIREWORK_LAUNCH` | `ENTITY_FIREWORK_ROCKET_LAUNCH` |
| `SHOOT_ARROW` | `ENTITY_ARROW_SHOOT` |
| `EXPLODE` | `ENTITY_GENERIC_EXPLODE` |
| `HURT_FLESH` | `ENTITY_PLAYER_HURT` |
| `LEVEL_UP` | `ENTITY_PLAYER_LEVELUP` |
| `CLICK` | `UI_BUTTON_CLICK` |
| `ANVIL_LAND` | `BLOCK_ANVIL_LAND` |
| `GLASS` | `BLOCK_GLASS_BREAK` |

**常见粒子名映射**（1.12 → 1.21）：

| 1.12 | 1.21 |
|---|---|
| `FLAME` | `FLAME`（未变） |
| `REDSTONE` | `DUST` |
| `CRIT_MAGIC` | `ENCHANTED_HIT` |
| `FIREWORKS_SPARK` | `FIREWORK` |
| `EXPLOSION_NORMAL` | `POOF` |
| `EXPLOSION_LARGE` | `EXPLOSION` |
| `EXPLOSION_HUGE` | `EXPLOSION_EMITTER` |
| `SMOKE_NORMAL` | `SMOKE` |
| `SMOKE_LARGE` | `LARGE_SMOKE` |
| `VILLAGER_HAPPY` | `HAPPY_VILLAGER` |
| `HEART` | `HEART`（未变） |
| `LAVA` | `LAVA`（未变） |

完整可验证的列表见 [Sound Javadoc](https://jd.papermc.io/paper/1.21.11/org/bukkit/Sound.html)
与 [Particle Javadoc](https://jd.papermc.io/paper/1.21.11/org/bukkit/Particle.html)。

**额外注意**：1.21 中部分粒子（如 `DUST`、`BLOCK`、`ITEM`、`ENTITY_EFFECT`）
需要附加数据对象才能生成。如果旧配置使用的粒子在迁移后**不显示但无报错**，
多半是该粒子类型需要数据而配置未提供——请把现象反馈给我确认。
