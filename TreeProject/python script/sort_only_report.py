import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


CSV_FILE = "benchmark_sort_only_results.csv"
OUTPUT_PNG = "sort_only_distributions_report.png"
ALGORITHMS = ["BST", "RBT", "QuickSort"]
COLORS = {
    "BST": "#3498db",
    "RBT": "#e74c3c",
    "QuickSort": "#2ecc71",
}


def get_value(subset, algo, column_name):
    algo_row = subset[(subset["Tree"] == algo) & (subset["Op"] == "Sort")]
    if algo_row.empty:
        return np.nan
    return float(algo_row[column_name].values[0])


def main():
    if not os.path.exists(CSV_FILE):
        raise FileNotFoundError(
            f"Could not find '{CSV_FILE}'. Run sort-only benchmark and export CSV first."
        )

    df = pd.read_csv(CSV_FILE)
    distributions = df["Distribution"].unique()

    fig, axes = plt.subplots(len(distributions), 1, figsize=(10, 5 * len(distributions)))
    if len(distributions) == 1:
        axes = [axes]

    for i, dist in enumerate(distributions):
        subset = df[df["Distribution"] == dist]

        means = [get_value(subset, algo, "Mean(ms)") for algo in ALGORITHMS]
        stds = [get_value(subset, algo, "StdDev(ms)") for algo in ALGORITHMS]

        x = np.arange(len(ALGORITHMS))
        colors = [COLORS[algo] for algo in ALGORITHMS]

        axes[i].bar(x, means, yerr=stds, capsize=5, color=colors, width=0.6)
        axes[i].set_ylabel("Time (ms)")
        axes[i].set_title(f"Sort-Only Distribution: {dist}")
        axes[i].set_xticks(x)
        axes[i].set_xticklabels(ALGORITHMS)
        axes[i].grid(axis="y", linestyle="--", alpha=0.6)

    plt.tight_layout()
    plt.savefig(OUTPUT_PNG, dpi=200)
    plt.show()


if __name__ == "__main__":
    main()
