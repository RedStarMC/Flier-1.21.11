# 动作（Action）

动作是游戏中涉及玩家的一次行为。它可以很简单，比如给引擎补充燃料；
也可以很复杂，比如发射一枚追踪导弹。动作定义在 _actions.yml_ 文件中。
每个动作都有一个 `type` 指定它做什么，附加选项指定它怎么做。
所有可用类型列在下方。

some_action:
  type: [动作类型]
  [类型专属设置]

一般来说动作之间没有共享设置，一切都取决于动作的类型。

## 攻击（Attack）

可以把其他动作施加到目标玩家身上的动作称为"攻击"。典型例子是机枪和
追踪导弹：当它们命中另一名玩家时，会施加一组定义好的动作（技术上
是 usage——带激活器的动作，见 _物品（Item）_ 一章）。这可以用来造成
伤害、打落翅膀、制造殃及其他玩家的爆炸等等。

攻击还有其他设置。`no_damage_ticks` 负责防止下一次攻击过快发生；
`friendly_fire` 控制该武器能否攻击友方玩家；`suicidal` 控制能否
攻击自己。

有一个特殊特性：`final`。它告诉 Flier 这次攻击是否应被视为一次"命中"。
这很重要，因为有一种情况你不希望某个攻击算命中——链式攻击。如果你的
一个攻击（比如火箭）触发了另一个攻击（比如爆炸），你会想跳过"火箭命中"
的提示，只显示被爆炸波及的玩家的提示。爆炸会覆盖到被火箭命中的玩家，
所以你不想看到两次消息。第一个攻击应标记为 `final: false`，
因为爆炸才是真正的命中。

_**注意**：如果你的 `no_damage_ticks` 设置大于 0，所有 final 命中都会
阻止后续攻击发生。这种情况下链式攻击必须禁用 `final` 才能正常工作。_

some_attack_action:
  type: [攻击动作类型]
  attack_usages:
    [usages]
  no_damage_ticks: [非负整数]
  friendly_fire: [true/false]
  suicidal: [true/false]
  final: [true/false]

* `attack_usages` 是一组"usage"列表。与 _物品（Item）_ 一章所述相同，
  但弹药和冷却等设置在这里不适用。
* `no_damage_ticks`（**默认：0**）玩家再次被攻击命中前需要经过的刻数。
* `friendly_fire`（**默认：true**）该武器能否攻击友方玩家。
* `suicidal`（**默认：false**）该武器能否攻击自己。
* `final`（**默认：true**）该攻击是否计为一次命中。

## 动作类型

### 效果（Effect）

**`effect`**

该动作在 `duration` 刻内以每秒 20 次的频率运行其他动作。
例如想让翅膀恢复量在一分钟内增加 1，就运行 `wingsHealth` 动作，
`amount` 为 `1`，`duration` 为 1200（20 刻/秒 × 60 秒）。

effect_action:
  type: effect
  actions:
  - [动作名列表]
  duration: [正整数]

* `actions` 每刻要施加的动作列表。
* `duration`（**必填**）这些动作运行的刻数。

### 燃料（Fuel）

**`fuel`**

该动作修改玩家引擎的燃料量。

fuel_action:
  type: fuel
  amount: [小数]

* `amount`（**必填**）要增加的燃料量（负数为扣除）。

### 弹射（Launch）

**`launch`**

该动作把你朝视线方向弹射出去。如果你站在地面上，会先把你抛起几米，
然后启用飞行。

launch_action:
  type: launch
  speed: [正小数]

* `speed`（**必填**）弹射速度。

### 滑跑起飞（Sprint starting）

**`sprintStarting`**

该动作允许玩家在跑道上冲刺时起飞。它需要被持续使用/激活才能工作
（例如在没有任何激活器的 usage 中）。玩家需要在平坦表面上沿直线冲刺。

玩家的冲刺速度会在 `time` 秒内提升到 `max` 速度，达到时会被轻轻抛入
空中。滑翔会启用，引擎会打开（如同玩家在潜行）。要关闭引擎，
按一下潜行键即可。

takeoff:
  type: sprintStarting
  max: [0-1 小数]
  time: [非负小数]

* `max`（**默认：0.8**）玩家起飞时的速度。
* `time`（**默认：5**）冲刺时达到 `max` 速度所需的秒数。

### 金钱（Money）

**`money`**

该动作给予（或扣除）玩家金钱。

money_action:
  type: money
  money: [整数]

* `money`（**必填**）要增加的金钱数。负数为扣除。

### 目标指南针（Target compass）

**`targetCompass`**

该动作让玩家的指南针指向最近的指定类型目标。由于目标（玩家）在移动，
最好反复运行此动作。

target_action:
  type: targetCompass
  target: [目标类型]

* `target`（**默认：hostile**）目标类型。可选值：
  * `hostile`（敌对）
  * `friendly`（友方）
  * `neutral`（中立）

### 翅膀耐久（Wings Health）

**`wingsHealth`**

该动作修改翅膀的耐久。耐久降到 0 时翅膀会损坏，回升到 0 以上时会恢复。

wings_health_action:
  type: wingsHealth
  amount: [小数]

* `amount`（**必填**）给翅膀增加的耐久量。负数为扣除。

### 卸除翅膀（Wings Off）

**`wingsOff`**

该动作把目标玩家的翅膀卸下来。

take_wings_off:
  type: wingsOff

### 生命值（Health）

**`health`**

该动作修改玩家的生命值。数值为负时伤害玩家（不会改变死亡原因，
所以其他玩家仍可获得击杀分数/金钱），为正时治疗玩家。它还可以根据
此动作的目标与来源之间的距离缩放数值。

health_action:
  type: health
  amount: [小数]
  distance_scale: [非负小数]
  min_amount: [小数]

* `amount`（**必填**）要修改的生命值。负数伤害玩家。
* `distance_scale`（**默认：0**）非 0 时会根据与来源的距离缩放伤害。
  距离为 0 时（可能是玩家以自己为目标）使用完整数值；距离等于此设置时，
  造成 `min_amount` 指定的数值；介于两者之间按比例缩放。
* `min_amount`（**默认：0**）按距离缩放时的最小伤害。必须在 0 与
  `amount` 之间，否则插件会改用 0 或 `amount`。

### 自杀（Suicide）

**`suicide`**

该动作直接杀死玩家。它不会改变此前的攻击者，所以击杀可能记在另一名
玩家头上。

self_destruction:
  type: suicide

### 物品套装（Item set）

**`itemSet`**

该动作按指定规则应用一个物品套装。可用设置与游戏按钮完全相同，
详见 _游戏（Game）_ 一章。

apply_set:
  type: itemSet
  item_set: [物品套装名]
  add_type: [添加方式]
  amount: [非负整数]
  conflict_action: [冲突处理]
  saving: [true/false]

### 消耗（Consume）

**`consume`**

该动作从玩家背包消耗一件物品，效果与使用可消耗物品相同。与设置为
减少单件物品的 `itemSet` 动作不同，它不会更新套装物品。

consume_item:
  type: consume
  item: [物品名]

* `item` 物品名，与 _items.yml_ 文件中一致。

### 分数（Score）

**`score`**

该动作给执行它的玩家加分或减分。具体如何处理加减分由游戏决定——
例如团队游戏会修改玩家所在队伍的分数。

remove_points:
  type: score
  amount: -10

* `amount`（**必填**）要加/减的分数。

### 追踪导弹（Homing missile）

**`homingMissile`**

该动作是一种攻击，发射一枚追踪弹射物。导弹起初直线飞行，发现目标后
会朝目标飞去。如果因某种原因丢失目标，它会绕圈飞行，直到寿命耗尽或
找到新目标。

rocket_action:
  type: homingMissile
  [攻击专属设置]
  entity: [弹射物类型]
  search_range: [正整数]
  search_radius: [正小数]
  speed: [正小数]
  lifetime: [正整数]
  maneuverability: [正小数]
  target_friends: [true/false]
  target_self: [true/false]

* `entity`（**必填**）攻击使用的
  [实体类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/entity/EntityType.html)
  （必须是弹射物，不能用羊之类的）。
* `search_range`（**必填**）导弹搜索目标的范围（技术上是导弹正前方
  一个球体的直径）。
* `search_radius`（**必填**）搜索目标时（丢失目标后）的机动性。
  数值越大，火箭绕的圈越小。
* `speed`（**必填**）导弹速度。
* `lifetime`（**必填**）导弹发射后的存活刻数。此时间内未命中任何东西
  就会消失。
* `maneuverability`（**必填**）追踪目标时的转向能力。数值越大，
  火箭跟踪目标的能力越强。
* `target_friends`（**默认：true**）导弹是否会以友方玩家为目标。
* `target_self`（**默认：true**）导弹是否会以发射它的玩家为目标。

### 弹射物枪械（Projectile Gun）

**`projectileGun`**

该动作是一种攻击，朝玩家视线方向发射一串直线飞行的弹射物子弹。

这种武器类型受到很大限制，原因有二：服务端处理弹射物碰撞的方式，
以及客户端对高速弹射物存在显示 bug。建议把下方介绍的粒子武器作为
主力武器，这个留给简单手枪之类。

gun_action:
  type: projectileGun
  [攻击专属设置]
  entity: [弹射物类型]
  burst_amount: [正整数]
  burst_ticks: [正整数]
  projectile_speed: [正小数]

* `entity`（**必填**）攻击使用的
  [实体类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/entity/EntityType.html)
  （必须是弹射物，不能用羊之类的）。
* `burst_amount`（**必填**）一次点射的子弹数。
* `burst_ticks`（**必填**）子弹之间的发射间隔刻数。
* `projectile_speed`（**必填**）子弹速度。

### 粒子枪械（Particle Gun）

**`particleGun`**

该动作是一种攻击，朝玩家视线方向发射一串直线飞行的粒子子弹。

gun_action:
  type: particleGun
  [攻击专属设置]
  particle: [粒子类型]
  amount: [非负整数]
  offset: [非负小数]
  speed: [非负小数]
  density: [正小数]
  burst_amount: [正整数]
  burst_ticks: [正整数]
  spread: [非负整数]
  projectile_speed: [正小数]
  proximity: [正小数]
  range: [正小数]

* `particle`（**必填**）
  [粒子类型](https://jd.papermc.io/paper/1.21.11/org/bukkit/Particle.html)。
* `amount`（**默认：0**）粒子数量。设为 0 启用替代粒子模式。
  高数值不会拖垮服务端（客户端是另一回事）。
* `offset`（**默认：0**）粒子随机生成的范围。为 0 时粒子精确出现在
  玩家位置。在替代模式下可能有不同含义。
* `offset_x`、`offset_y`、`offset_z`（**默认：0**）覆盖特定轴的
  偏移，在替代模式下有用，例如设置颜色。
* `speed`（**默认：0**）粒子速度。在替代模式下可能有不同含义。
* `density`（**默认：0.5**）子弹路径上每格距离的粒子数。
  设得太高会拖垮服务端。
* `burst_amount`（**必填**）一次点射的子弹数。
* `burst_ticks`（**必填**）点射的总时长。
* `spread`（**默认：0**）子弹的精度，0 表示完美精确。
* `projectile_speed`（**必填**）子弹速度。
* `proximity`（**默认：1**）子弹必须距离玩家多近才算命中。
* `range`（**默认：256**）子弹射程——飞行该距离（格）后消失。

### 炸弹（Bomb）

**`bomb`**

该动作是一种攻击，生成一个会爆炸的 TNT。

bomb_action:
  type: bomb
  [攻击专属设置]
  power: [正小数]
  fuse: [非负整数]

* `power`（**必填**）爆炸威力。它不控制伤害，只控制爆炸半径。
* `fuse`（**默认：80**）爆炸前的刻数。

### 爆炸（Explosion）

**`explosion`**

该攻击影响爆炸来源周围指定半径内的所有玩家。它不是视觉爆炸——
不过你可以用效果（Effect）机制添加视觉表现。

explode:
  type: explosion
  [攻击专属设置]
  radius: [正小数]

* `radius`（**必填**）爆炸来源周围的影响半径。
