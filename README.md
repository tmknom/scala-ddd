# scala-ddd

[![CircleCI](https://circleci.com/gh/tmknom/scala-ddd/tree/master.svg?style=svg)](https://circleci.com/gh/tmknom/scala-ddd/tree/master)
[![Coverage Status](https://coveralls.io/repos/github/tmknom/scala-ddd/badge.svg?branch=master)](https://coveralls.io/github/tmknom/scala-ddd?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/2f12ab859a1d41f2aaf95748741125b6)](https://www.codacy.com/app/tmknom/scala-ddd)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2f12ab859a1d41f2aaf95748741125b6)](https://www.codacy.com/app/tmknom/scala-ddd)


## Description

ScalaでDDDを実践するよ！


## Requirement

* Java8
* sbt


## Usage

### コンパイル

```bash
sbt compile
```

### テスト

```bash
sbt test
```

### カバレッジの生成

```bash
sbt clean coverage test coverageReport
```

### 静的解析：Scalastyle

```bash
sbt scalastyle
```

### 静的解析：Scapegoat

```bash
sbt scapegoat
```

### コピペチェック

```bash
sbt cpd
```

### 依存ライブラリのアップデートチェック

```bash
sbt dependencyUpdates
```

### 依存ライブラリの脆弱性チェック

```bash
sbt dependencyCheck
```

### コードの統計情報取得

```bash
sbt stats
```

### 依存関係のグラフの画像出力

[graphviz](http://www.graphviz.org/) 必須。

```bash
sbt dependencyDot
dot -Kdot -Tpng target/dependencies-compile.dot -o target/dependencies-compile.png
```


## Installation

```bash
git clone git@github.com:tmknom/scala-ddd.git && cd scala-ddd
sbt compile
```
