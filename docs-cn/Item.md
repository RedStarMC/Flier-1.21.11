# 物品（Item）

Flier 中的物品由普通的 Minecraft 物品来表示。物品的大部分设置决定
这个 Minecraft 物品的外观：材质、名称、Lore 等。

游戏内的物品没有默认行为。取而代之的是，它们可以指定所谓的
"usage（用法）"。每个用法由一组激活器（Activator）和一组动作（Action）
组成。当所有激活器都被激活时，所有动作就会执行。
详见 _动作（Actions）_ 和 _激活器（Activators）_ 章节。

有些物品可以有弹药。如果有，每个用法都可以消耗一定数量的弹药。
弹药不足时用法无法激活。弹药耗尽后，物品将变为不可用或被消耗掉
（取决于 `consumable` 选项）。你也可以设置弹药消耗为负数的用法，
这种用法会恢复弹药。

item_name:
  material: [材料类型]
  name: [可翻译文本]
  lore:
  - [可翻译文本列表]
  weight: [小数]
  slot: [整数]
  amount: [正整数]
  starting_cooldown: [非负整数]
  max_amount: [非负整数]
  min_amount: [非负整数]
  consumable: [true/false]
  ammo: [非负整数]
  usages:
    first_usage:
      cooldown: [非负整数]
      ammo_use: [整数]
      where: [位置条件]
      activators:
      - [激活器名列表]
      actions:
      - [动作名列表]

## 物品设置

第一条分隔线之前的设置适用于所有物品——包括引擎和翅膀。
两条分隔线之间的设置只适用于可用物品（usable item）。
第二条分隔线之后的"用法"（usages）不仅用于可用物品，
也用于某些动作（Action）和增益道具（Bonus）。

* `material`（**必填**）物品的
  [材料](https://jd.papermc.io/paper/1.21.11/org/bukkit/Material.html)类型。
* `name`（**必填**）物品名称。支持颜色代码（`&`）。
* `lore`（**可选**）Lore 行列表。Lore 是物品名称下方的那段文字，
  默认是紫色斜体。
* `weight`（**默认：0**）物品重量。携带的物品越重，
  飞行时被拉向地面的力就越大。
* `slot`（**默认：-1**）物品放置的槽位。留空或设为负数时，
  该物品会被自动放置（不会放在快捷栏上）。玩家无法在槽位之间移动物品。
  唯一的例外是翅膀，详见 _翅膀（Wings）_ 一章。

***

* `consumable`（**默认：false**）物品在使用后（或弹药耗尽后）
  是否会被消耗掉。
* `amount`（**默认：1**）给予玩家的该物品数量。
* `starting_cooldown`（**默认：0**）重生后首次使用前的冷却刻数。
* `max_amount`（**默认：0**）玩家最多可持有该物品的数量。`0` 表示不限。
* `min_amount`（**默认：0**）当允许出售时，玩家最多能卖到剩余该数量为止。
* `ammo`（**默认：0**）该物品的最大弹药量。

***

* `usages` 用法列表。
  * `cooldown`（**默认：0**）激活该用法后的冷却时间（单位：刻），
    冷却期间无法激活其他用法。
  * `ammo_use`（**默认：0**）每次使用消耗的弹药量。负数会恢复弹药。
  * `where`（**默认：everywhere**）玩家必须处于什么位置状态才能激活
    该用法。可接受的值：
    * `everywhere`（其实应该叫 "anywhere"，我知道）
    * `air`（空中）
    * `ground`（地面）
    * `fall`（下落中）
    * `no air`（地面或下落）
    * `no ground`（空中或下落）
    * `no fall`（空中或地面）
  * `activators` 激活器列表，定义在 _activators.yml_ 中。
  * `actions` 动作列表，定义在 _actions.yml_ 中。

## 迁移提示：`material` 与 1.21 材料名

`material` 的值必须是 1.21 的 Material 枚举名。1.12 时代配置中的
旧材料名（以及"材料:数据值"写法）在加载时会直接报
"Material does not exist" 错误，错误信息会指出具体配置节。常见映射：

| 1.12 写法 | 1.21 写法 |
|---|---|
| `STAINED_GLASS_PANE:7` | `GRAY_STAINED_GLASS_PANE` |
| `WOOL:5` | `LIME_WOOL` |
| `SKULL_ITEM:3` | `PLAYER_HEAD` |
| `WATCH` | `CLOCK` |
| `INK_SACK:4` | `LAPIS_LAZULI` |
| `INK_SACK:15` | `BONE_MEAL` |
| `ENCHANTMENT_TABLE` | `ENCHANTING_TABLE` |
| `WORKBENCH` | `CRAFTING_TABLE` |
| `THIN_GLASS` | `GLASS_PANE` |
| `EXP_BOTTLE` | `EXPERIENCE_BOTTLE` |
| `SULPHUR` | `GUNPOWDER` |
| `FIREWORK` | `FIREWORK_ROCKET` |

完整可验证的材料名列表见 Paper 1.21.11 的
[Material Javadoc](https://jd.papermc.io/paper/1.21.11/org/bukkit/Material.html)。

**注意**：物品名称（`name`）支持 `&` 颜色代码；如果迁移后颜色代码
不生效（显示为字面 `&c`），说明文本组件化处理需要调整，请在控制台
查找相关报错或警告。
