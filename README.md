# scala-ddd

[![CircleCI](https://circleci.com/gh/tmknom/scala-ddd/tree/master.svg?style=svg)](https://circleci.com/gh/tmknom/scala-ddd/tree/master)
[![Coverage Status](https://coveralls.io/repos/github/tmknom/scala-ddd/badge.svg?branch=master)](https://coveralls.io/github/tmknom/scala-ddd?branch=master)


## Description

ScalaでDDDを実践するよ！


## Requirement

* Java8
* Activator


## Usage

### コンパイル

```bash
activator compile
```

### テスト

```bash
activator test
```

### カバレッジの生成

```bash
activator clean coverage test coverageReport
```

### 静的解析：Scalastyle

```bash
activator scalastyle
```

### コピペチェック

```bash
activator cpd
```

### 依存ライブラリのアップデートチェック

```bash
activator dependencyUpdates
```

### コードの統計情報取得

```bash
activator stats
```


## Installation

```bash
git clone git@github.com:tmknom/scala-ddd.git && cd scala-ddd
activator compile
```
