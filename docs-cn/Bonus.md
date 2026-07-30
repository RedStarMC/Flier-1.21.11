# 增益道具（Bonus）

增益道具是游戏地图上的可拾取对象，玩家拾取后会运行一些动作。
每个增益道具可以指定再次拾取前的冷却、重生时间和动作列表。
不同的增益道具类型决定它在世界中如何呈现——例如以实体形式（比如一只羊）。

注意：如果增益道具是通过进入其半径来拾取的、`cooldown` 为 0，
且该道具不可消耗，那么只要玩家在它附近，所有动作就会每刻运行一次
（每秒 20 次）。可以利用这一点制作治疗或补燃料类的增益道具。

some_bonus:
  type: [增益道具类型]
  consumable: [true/false]
  cooldown: [非负整数]
  respawn: [非负整数]
  actions:
  - [动作名列表]
  [类型专属设置]

## 增益道具设置

* `consumable`（**必填**）拾取后增益道具是否消失。
* `cooldown`（**必填**）玩家再次拾取该道具前需要等待的刻数。
* `respawn`（**必填**）道具被拾取后重生所需的刻数。

## 增益道具类型

### 隐形（Invisible）

**`invisible`**

该类型的增益道具不可见，通过在指定位置附近即可拾取。

invisible_bonus:
  type: invisible
  [默认增益道具设置]
  location: [坐标]
  distance: [正小数]

* `location`（**必填**）道具所在位置（引用 _arenas.yml_ 中的坐标名）。
* `distance`（**必填**）玩家距离道具多近时会拾取它。

### 实体（Entity）

**`entity`**

该类型的增益道具以一个旋转、悬浮的实体呈现（比如一只羊）。
同样通过靠近指定位置拾取。

entity_bonus:
  type: entity
  [默认增益道具设置]
  entity: [实体类型]
  location: [坐标]
  distance: [正小数]

* `entity`（**必填**）
  [实体类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/entity/EntityType.html)。
* `location`（**必填**）道具所在位置。
* `distance`（**必填**）玩家距离道具多近时会拾取它。

**迁移提示**：`entity` 必须使用 1.21 的 EntityType 枚举名
（如 `SHEEP`、`CHICKEN`、`ARMOR_STAND`）。1.12 时代的实体名大多不变，
但少数有调整（如 `PigZombie` → `ZOMBIFIED_PIGLIN`），
完整列表见上方 Javadoc。实体在 1.21 上的默认行为（AI、重力等）与
1.12 有差异——如果测试中发现增益实体乱飞、消失或坠落，
可能需要在实现中为该实体设置 `AI=false`、`gravity=false`、
`invulnerable=true`，请反馈具体现象。
