from matplotlib.backends.backend_pdf import PdfPages
import matplotlib.pyplot as plt
import numpy as np


def main():
    # TODO individual histogram colors to match percentage colors
    # TODO darker/bolder titles on individual histograms
    figs = []

    # BioKG
    p_counts = [12, 0, 1, 0, 4]
    all_counts = [2015813, 0, 28033, 0, 24152]
    train_counts = [2005708, 0, 27926, 0, 24024]
    valid_counts = [5065, 0, 55, 0, 50]
    test_counts = [5040, 0, 52, 0, 78]
    figs.append(single_histogram(p_counts, "BioKG p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "BioKG"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "BioKG"))

    # FB13
    p_counts = [8, 2, 1, 0, 2]
    all_counts = [200148, 66753, 66663, 0, 12309]
    train_counts = [177747, 66753, 59423, 0, 12309]
    valid_counts = [4464, 0, 1444, 0, 0]
    test_counts = [17937, 0, 5796, 0, 0]
    figs.append(single_histogram(p_counts, "FB13 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB13"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB13"))

    # FB15k
    p_counts = [38, 43, 35, 18, 1211]
    all_counts = [43655, 17499, 34833, 3626, 492600]
    train_counts = [35585, 14275, 28276, 2970, 402036]
    valid_counts = [3685, 1477, 2989, 336, 41513]
    test_counts = [4385, 1747, 3568, 320, 49051]
    figs.append(single_histogram(p_counts, "FB15k p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB15k"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB15k"))

    # FB15k-237
    p_counts = [95, 53, 29, 29, 31]
    all_counts = [141590, 91518, 28811, 29440, 18757]
    train_counts = [118392, 82075, 26344, 28145, 17159]
    valid_counts = [10732, 4309, 1146, 594, 754]
    test_counts = [12466, 5134, 1321, 701, 844]
    figs.append(single_histogram(p_counts, "FB15k-237 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB15k-237"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB15k-237"))

    # Hetionet
    p_counts = [22, 0, 1, 0, 1]
    all_counts = [2050109, 0, 102240, 0, 97848]
    train_counts = [2039827, 0, 101737, 0, 97382]
    valid_counts = [5164, 0, 238, 0, 224]
    test_counts = [5118, 0, 265, 0, 242]
    figs.append(single_histogram(p_counts, "Hetionet p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "Hetionet"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "Hetionet"))

    # NELL-995
    p_counts = [154, 24, 11, 10, 1]
    all_counts = [123018, 19352, 7573, 3468, 802]
    train_counts = [121075, 17244, 7089, 3468, 802]
    valid_counts = [230, 254, 59, 0, 0]
    test_counts = [1713, 1854, 425, 0, 0]
    figs.append(single_histogram(p_counts, "NELL-995 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "NELL-995"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "NELL-995"))

    # WN11
    p_counts = [2, 0, 0, 1, 8]
    all_counts = [2851, 0, 0, 40161, 82722]
    train_counts = [2758, 0, 0, 36178, 73645]
    valid_counts = [14, 0, 0, 816, 1779]
    test_counts = [79, 0, 0, 3167, 7298]
    figs.append(single_histogram(p_counts, "WN11 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN11"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN11"))

    # WN18
    p_counts = [4, 0, 0, 0, 14]
    all_counts = [34569, 0, 0, 0, 116873]
    train_counts = [32232, 0, 0, 0, 109210]
    valid_counts = [1165, 0, 0, 0, 3835]
    test_counts = [1172, 0, 0, 0, 3828]
    figs.append(single_histogram(p_counts, "WN18 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN18"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN18"))

    # WN18RR
    p_counts = [11, 0, 0, 0, 0]
    all_counts = [93003, 0, 0, 0, 0]
    train_counts = [86835, 0, 0, 0, 0]
    valid_counts = [3034, 0, 0, 0, 0]
    test_counts = [3134, 0, 0, 0, 0]
    figs.append(single_histogram(p_counts, "WN18RR p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN18RR"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN18RR"))

    # YAGO3-10
    p_counts = [28, 5, 1, 2, 1]
    all_counts = [302091, 16396, 66771, 379733, 324049]
    train_counts = [299243, 16261, 66163, 376349, 321024]
    valid_counts = [1395, 63, 311, 1698, 1533]
    test_counts = [1453, 72, 297, 1686, 1492]
    figs.append(single_histogram(p_counts, "YAGO3-10 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "YAGO3-10"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "YAGO3-10"))

    # save all of the figures to one pdf
    with PdfPages('C:/Users/lklec/OneDrive/Desktop/Anomaly Papers/Histograms/output.pdf') as pdf:
        for fig in figs:
            pdf.savefig(fig, bbox_inches='tight')


def anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name):
    bins = [0.0, 0.2, 0.4, 0.6, 0.8, 1.0]
    counts = [all_counts, train_counts, valid_counts, test_counts]

    fig, ax_dict = plt.subplot_mosaic(
        [
            ['All Triples', 'Training'],
            ['Validation', 'Test']
        ],
        layout="constrained")

    itr = 0
    for k, ax in ax_dict.items():
        values, bns, bars = ax.hist(bins[:-1], bins, weights=counts[itr], rwidth=.99)
        ax.bar_label(bars, fmt='%d')
        ax.margins(y=0.1)
        itr += 1

    kw = dict(ha="center", va="center", fontsize=14, color="grey")
    for k, ax in ax_dict.items():
        ax.text(0.5, 0.75, k, transform=ax.transAxes, **kw)

    fig.suptitle(dataset_name + " Anomaly Distribution Across Splits")
    #plt.show()
    return fig


def single_histogram(counts, title):
    bins = [0.0, 0.2, 0.4, 0.6, 0.8, 1.0]
    fig, ax = plt.subplots(layout='constrained')
    values, bns, bars = ax.hist(bins[:-1], bins, weights=counts, rwidth=.99)
    ax.bar_label(bars)
    ax.set_xlabel("Anomaly Coefficient")
    ax.set_ylabel("Number of Relations, p")
    ax.set_title(title)
    #plt.show()
    return fig


def percentify(counts, total):
    for c in range(len(counts)):
        counts[c] = counts[c]/total * 100
    return counts


def percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name):
    p_count = sum(p_counts)
    all_count = sum(all_counts)
    train_count = sum(train_counts)
    valid_count = sum(valid_counts)
    test_count = sum(test_counts)
    labels = ("[0.0-0.2)", "[0.2-0.4)", "[0.4-0.6)", "[0.6-0.8)", "[0.8-1.0]")
    percents = {
        'p': percentify(p_counts, p_count),
        'All': percentify(all_counts, all_count),
        'Train': percentify(train_counts, train_count),
        'Valid': percentify(valid_counts, valid_count),
        'Test': percentify(test_counts, test_count)
    }
    x = np.arange(len(labels))
    width = 0.15
    multiplier = 0
    fig, ax = plt.subplots(layout='constrained')
    curr_color = 0
    colors = ["silver", "purple", "xkcd:turquoise", "xkcd:orange", "yellowgreen"]

    for attribute, measurement in percents.items():
        offset = width * multiplier
        rects = ax.bar(x + offset, measurement, width, label=attribute, color=colors[curr_color])
        ax.bar_label(rects, padding=3, fmt='{:.0f}', fontsize=8)
        multiplier += 1
        curr_color += 1

    ax.set_ylabel('Percent of data (%)')
    ax.set_xlabel('Anomaly coefficient value')
    ax.set_title(dataset_name + ': Distribution of anomalies by data split')
    ax.set_xticks(x + width*2, labels)
    ax.margins(y=0.2)
    ax.legend(loc='upper left', ncols=5)

    #plt.show()
    return fig


if __name__ == '__main__':
    main()
