import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import os

df = pd.read_csv('benchmark_results.csv')
distributions = df['Distribution'].unique()
fig, axes = plt.subplots(len(distributions), 1, figsize=(10, 6 * len(distributions)))
if len(distributions) == 1:
    axes = [axes]

for i, dist in enumerate(distributions):
    subset = df[df['Distribution'] == dist]

    ops = ['Insert', 'Search', 'Delete', 'Sort']
    bst_data = subset[subset['Tree'] == 'BST']
    rbt_data = subset[subset['Tree'] == 'RBT']

    bst_means = [bst_data[bst_data['Op'] == op]['Mean(ms)'].values[0] for op in ops]
    rbt_means = [rbt_data[rbt_data['Op'] == op]['Mean(ms)'].values[0] for op in ops]
    bst_std = [bst_data[bst_data['Op'] == op]['StdDev(ms)'].values[0] for op in ops]
    rbt_std = [rbt_data[rbt_data['Op'] == op]['StdDev(ms)'].values[0] for op in ops]

    x = np.arange(len(ops))
    width = 0.35

    axes[i].bar(x - width/2, bst_means, width, yerr=bst_std, label='BST', capsize=5, color='#3498db')
    axes[i].bar(x + width/2, rbt_means, width, yerr=rbt_std, label='RBT', capsize=5, color='#e74c3c')

    axes[i].set_ylabel('Time (ms)')
    axes[i].set_title(f'Distribution: {dist}')
    axes[i].set_xticks(x)
    axes[i].set_xticklabels(ops)
    axes[i].legend()
    axes[i].grid(axis='y', linestyle='--', alpha=0.6)

plt.tight_layout()
plt.savefig('all_distributions_report.png')
plt.show()