# 命令

Flier 只使用一个主命令 `/flier`。不带任何参数输入该命令可查看所有可用子命令。
只会显示你有权限使用的子命令——例如没有 `reload` 权限就不会显示 `reload`，
从控制台执行时不会显示 `coordinator`。

`/flier` 的别名是 `/f` 和 `/fl`。使用该命令的权限是 `flier.command`，
默认对所有玩家开放。

命令中以不同字体显示的部分需要替换为一个不含空格的单词。
方括号中的单词表示可选。

## 子命令

| 命令 | 别名 | 权限 | 使用者 | 描述 |
|---------------|--------|---------------|--------|---------------------------|
| _/flier lobby join `大厅` `[玩家]`_ | `l j` | `flier.player.join` / `flier.admin.join` | 玩家/管理员 | 将你传送到指定名称的大厅，或强制另一名玩家进入该大厅。 |
| _/flier lobby leave `[玩家]`_ | `l l` | `flier.player.leave` / `flier.admin.leave` | 玩家/管理员 | 将你移出当前大厅，或强制另一名玩家离开其所在大厅。 |
| _/flier lobby item `sell/buy` `物品` `[玩家]`_ | `l i` | `flier.player.item` / `flier.admin.item` | 玩家/管理员 | 为你购买/出售指定物品（在 _lobbies.yml_ 的 `buttons` 节中定义），或强制另一名玩家购买/出售。玩家金钱不足时会失败。 |
| _/flier lobby start `[玩家]`_ | `l s` | `flier.player.start` / `flier.admin.start` | 玩家/管理员 | 将你送入当前游戏，或强制另一名玩家进入游戏。 |
| _/flier money `玩家` `金额`_ | `m` | `flier.admin.setmoney` | 管理员 | 设置指定玩家的当前金钱。 |
| _/flier coordinator_ | `c` | `flier.admin.coordinator` | 管理员 | 开关一个工具：开启后点击方块时会在聊天栏显示可复制的坐标。 |
| _/flier reload_ | 无 | `flier.admin.reload` | 管理员 | 重载配置文件。 |

`save`、`load` 等开发用子命令未在此列出。
