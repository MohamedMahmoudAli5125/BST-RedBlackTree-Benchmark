<div align="center">
<img src="./assets/header.svg" width="100%"/>

[![Java](https://img.shields.io/badge/Java-OpenJDK%2017-e8491d?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-25a162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Alexandria University](https://img.shields.io/badge/Alexandria%20University-CSE-1a3a6b?style=for-the-badge)](#)

</div>

---

## Overview

A rigorous benchmark comparing **Binary Search Trees** and **Red-Black Trees** across four input distributions (Random, 1%/5%/10% Disorder) with n = 100,000 integers. Both trees implement a shared `ITree` interface for a fair comparison.

| Scenario | BST | RBT | Winner |
|----------|:---:|:---:|:------:|
| Random insert | ~30ms | ~35ms | 🏆 BST |
| 1% disorder insert | **~376ms** | ~16ms | 🏆 RBT ×24 |
| Height (sorted input) | **~100,000** | ~21 | 🏆 RBT |

---

## Key Results

```
BST Height on sorted data  : ~100,000  (degenerates to linked list)
RBT Height on all inputs   : ~18–22    (bounded by 2·log₂(n+1))
Speed-up (RBT / BST) on 1% disorder:
  Insert  ████████████████████████  ×24
  Search  ███████                   ×7
  Delete  ██████                    ×6
```

---

## Architecture

```
src/
├── ArrayGeneration/   Array generation utilities (seed = 12345)
├── BenchMark/         Benchmark runner, stats collector, reporting
├── CsvGeneration/     CSV export for benchmark results
├── Terminal/          Terminal/interactive menu interface
├── bst/               BinarySearchTree implementation & nodes
├── rbtree/            RedBlackTree implementation & nodes
└── ITree.java         Shared interface for both tree types
```

---

## Getting Started

```bash
git clone https://github.com/MohamedMahmoudAli5125/BST-RedBlackTree-Benchmark.git
cd BST-RedBlackTree-Benchmark
mvn clean package -DskipTests
mvn test                                        # run unit tests
java -jar target/tree-assignment-1.0-all.jar    # interactive menu
```

Select **Option 2** for full benchmark mode → exports `benchmark_results.csv`.

---

## Methodology

- **Seed:** `java.util.Random(12345)` — identical datasets for both trees across all runs
- **Runs:** 5 per configuration · reports Mean, Median, StdDev
- **JVM Warmup:** one full cycle discarded before measurement
- **Validation:** `VALIDATE = false` during benchmarks (O(n) checks fully disabled)
- **Logging:** SLF4J at `WARN` level — zero I/O overhead

---

## Conclusion

> **For random data:** BST is marginally faster (no rotation overhead).
> **For ordered/nearly-sorted data:** RBT is the only viable option — up to **24× faster** on 1%-disorder input.
> Use BST only when input is guaranteed random. In all other cases, choose RBT.

---

## References

- CLRS — *Introduction to Algorithms*, 3rd Ed., Chapters 12–13
- [GeeksForGeeks — BST & RBT Operations](https://www.geeksforgeeks.org/dsa/)
- [Baeldung — JUnit 5](https://www.baeldung.com/junit-5)
- [MaxGCoding — Validating Red-Black Trees](https://www.maxgcoding.com/validating-red-black-trees)

---


</div>
