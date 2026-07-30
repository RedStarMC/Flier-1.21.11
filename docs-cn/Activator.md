# 激活器（Activator）

激活器只做一件事：检查条件。它们用在 usage 中。当 usage 里所有激活器
都激活时，该 usage 就会被使用（并运行其动作）。激活器的 `type` 指定它
需要什么条件才能激活，附加设置指定何时精确激活。

技术上讲，usage 中的激活器每刻（每秒 20 次）都会被检查。如果 usage 里
没有任何激活器，它会一直每秒运行 20 次——务必小心这一点。有一个特殊的
`trigger` 激活器，可以配置为仅在玩家按下对应鼠标键后激活；还有一个
`interval` 激活器，每 _n_ 刻才激活一次，用它可以让 usage 降低运行频率。
注意：`trigger` 和 `interval` 激活器（按此顺序）组合时只有第 _n_ 次触发
才生效（顺序反过来时，每次触发只有 _1/n_ 的概率生效）。

## 激活器类型

### 触发（Trigger）

**`trigger`**

指定的触发事件发生时激活。目前有以下触发类型：

* `left_click`（左键）
* `right_click`（右键）

leftClick:
  type: trigger
  trigger: [触发类型]

### 间隔（Interval）

**`interval`**

每 _n_ 次检查激活一次。由于激活器按定义顺序每刻检查，且一旦某个激活器
失败检查就立即失败，所以 `interval` 的位置至关重要。例如放在 `trigger`
激活器**之后**时，它会每 _n_ 次触发放行一次；但放在**之前**时行为是
未定义的，因为你永远不知道 `trigger` 会不会恰好发生在第 _n_ 刻。

slow:
  type: interval
  interval: [正整数]

### 翅膀耐久（Wings health）

**`wingsHealth`**

翅膀耐久在 `min` 和 `max` 之间时激活。数值可以是绝对值，
也可以是最大耐久的百分比。

wing_health:
  type: wingsHealth
  min: [非负小数]
  max: [非负小数]
  number_type: [数值类型]

* `min`（**必填**）可接受耐久水平的下限。
* `max`（**默认：`min` 的值**）可接受耐久水平的上限。
* `number_type`（**默认：`absolute`**）数值类型。可选值：
    * `absolute`（绝对值）
    * `percentage`（百分比）

### 物品（Item）

**`item`**

玩家背包里有指定物品时激活。

has_item:
  type: item
  item: [物品名]

* `item` 物品名，与 _items.yml_ 文件中一致。

### 站在方块上（Standing on a block）

**`blockStanding`**

玩家站在指定方块上时激活。

on_a_block:
  type: blockStanding
  block: [方块类型]

* `block` [方块类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/Material.html)。

## 迁移提示：实体名 / 粒子名 / 材料名

本章与 _动作（Action）_ 一章中有三类值必须使用 **1.21 枚举名**，
1.12 旧配置的值会在加载时报错：

1. **实体名**（`homingMissile`、`projectileGun` 的 `entity`）：
   常见弹射物在 1.21 中的名字如 `ARROW`、`SNOWBALL`、`FIREBALL`、
   `SMALL_FIREBALL`、`WITHER_SKULL`、`FIREWORK_ROCKET`。
   完整列表见 [EntityType Javadoc](https://jd.papermc.io/paper/1.21.11/org/bukkit/entity/EntityType.html)。
2. **粒子名**（`particleGun` 的 `particle`）：1.13 后大量粒子改名，
   例如 `CRIT_MAGIC` → `ENCHANTED_HIT`、`REDSTONE` → `DUST`、
   `FIREWORKS_SPARK` → `FIREWORK`。遇到加载报错按
   [Particle Javadoc](https://jd.papermc.io/paper/1.21.11/org/bukkit/Particle.html)
   逐个替换。
3. **方块材料名**（`blockStanding` 的 `block`）：与物品材料相同的
   扁平化映射，例如 `GRASS` → `SHORT_GRASS`、`STONE` 不变。
   见 _物品（Item）_ 一章的映射表。
