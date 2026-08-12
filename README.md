# 跳舞的树（DancingTree）

适用于 Paper 1.20+ 的轻量插件（Paper 1.20.6+ 需要 Java 21）。玩家在树苗旁反复蹲起，达到次数后尝试催熟，并显示骨粉粒子。

只有服务器原生树生成器判断可以正常长成树的树苗才会催熟；放在石头等不符合原版种植条件上的树苗不会被催熟。默认需要蹲 8 次，连续蹲起间隔超过 5 秒会重新计数，参数可在 `plugins/DancingTree/config.yml` 调整。

## 构建

```bash
./gradlew build
```

插件位于 `build/libs/DancingTree-1.0.0.jar`。

## 许可

MIT License，作者 Snowball_233。
