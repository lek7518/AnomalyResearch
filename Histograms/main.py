from matplotlib.backends.backend_pdf import PdfPages
import matplotlib.pyplot as plt
import numpy as np


def main():
    '''
    figs = []

    # BioKG
    # using counts for just near-same, near-reverse, and cartesian anomalies
        # p_counts = [12, 0, 1, 0, 4]
        # all_counts = [2015813, 0, 28033, 0, 24152]
        # train_counts = [2005708, 0, 27926, 0, 24024]
        # valid_counts = [5065, 0, 55, 0, 50]
        # test_counts = [5040, 0, 52, 0, 78]
    # using counts for near-same, near-reverse, cartesian, reflexive, transitive, duplicate, and symmetric anomalies
    p_counts = [10, 0, 1, 0, 6]
    all_counts = [567911, 0, 28033, 0, 1472054]
    train_counts = [565277, 0, 27926, 0, 1464455]
    valid_counts = [1298, 0, 55, 0, 3817]
    test_counts = [1336, 0, 52, 0, 3782]
    figs.append(single_histogram(p_counts, "BioKG p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "BioKG"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "BioKG"))

    # FB13
    # p_counts = [8, 2, 1, 0, 2]
    # all_counts = [200148, 66753, 66663, 0, 12309]
    # train_counts = [177747, 66753, 59423, 0, 12309]
    # valid_counts = [4464, 0, 1444, 0, 0]
    # test_counts = [17937, 0, 5796, 0, 0]
    p_counts = [7, 2, 1, 0, 3]
    all_counts = [195684, 66753, 66663, 0, 16773]
    train_counts = [173283, 66753, 59423, 0, 16773]
    valid_counts = [4464, 0, 1444, 0, 0]
    test_counts = [17937, 0, 5796, 0, 0]
    figs.append(single_histogram(p_counts, "FB13 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB13"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB13"))

    # FB15k
    # p_counts = [38, 43, 35, 18, 1211]
    # all_counts = [43655, 17499, 34833, 3626, 492600]
    # train_counts = [35585, 14275, 28276, 2970, 402036]
    # valid_counts = [3685, 1477, 2989, 336, 41513]
    # test_counts = [4385, 1747, 3568, 320, 49051]
    p_counts = [26, 32, 28, 16, 1243]
    all_counts = [35154, 9641, 10891, 2732, 533795]
    train_counts = [28686, 7892, 8885, 2237, 435442]
    valid_counts = [2959, 797, 943, 244, 45057]
    test_counts = [3509, 952, 1063, 251, 53296]
    figs.append(single_histogram(p_counts, "FB15k p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB15k"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB15k"))

    # FB15k-237
    # p_counts = [95, 53, 29, 29, 31]
    # all_counts = [141590, 91518, 28811, 29440, 18757]
    # train_counts = [118392, 82075, 26344, 28145, 17159]
    # valid_counts = [10732, 4309, 1146, 594, 754]
    # test_counts = [12466, 5134, 1321, 701, 844]
    p_counts = [82, 48, 28, 26, 53]
    all_counts = [125093, 74169, 28537, 20774, 61543]
    train_counts = [103522, 64631, 25542, 19555, 58865]
    valid_counts = [9973, 4363, 1384, 563, 1252]
    test_counts = [11598, 5175, 1611, 656, 1426]
    figs.append(single_histogram(p_counts, "FB15k-237 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "FB15k-237"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "FB15k-237"))

    # Hetionet
    # p_counts = [22, 0, 1, 0, 1]
    # all_counts = [2050109, 0, 102240, 0, 97848]
    # train_counts = [2039827, 0, 101737, 0, 97382]
    # valid_counts = [5164, 0, 238, 0, 224]
    # test_counts = [5118, 0, 265, 0, 242]
    p_counts = [17, 0, 2, 3, 2]
    all_counts = [1568554, 0, 163930, 154193, 363520]
    train_counts = [1560662, 0, 163150, 153445, 361689]
    valid_counts = [3950, 0, 379, 386, 911]
    test_counts = [3942, 0, 401, 362, 920]
    figs.append(single_histogram(p_counts, "Hetionet p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "Hetionet"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "Hetionet"))

    # NELL-995
    # p_counts = [154, 24, 11, 10, 1]
    # all_counts = [123018, 19352, 7573, 3468, 802]
    # train_counts = [121075, 17244, 7089, 3468, 802]
    # valid_counts = [971, 1054, 242, 0, 0]
    # test_counts = [972, 1054, 242, 0, 0]
    p_counts = [129, 31, 14, 22, 4]
    all_counts = [98104, 24683, 10101, 14369, 6956]
    train_counts = [96161, 22575, 9617, 14369, 6956]
    valid_counts = [230, 254, 59, 0, 0]
    test_counts = [1713, 1854, 425, 0, 0]
    figs.append(single_histogram(p_counts, "NELL-995 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "NELL-995"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "NELL-995"))

    # WN11
    # p_counts = [2, 0, 0, 1, 8]
    # all_counts = [2851, 0, 0, 40161, 82722]
    # train_counts = [2758, 0, 0, 36178, 73645]
    # valid_counts = [14, 0, 0, 816, 1779]
    # test_counts = [79, 0, 0, 3167, 7298]
    p_counts = [1, 0, 0, 1, 9]
    all_counts = [1169, 0, 0, 40161, 84404]
    train_counts = [1099, 0, 0, 36178, 75304]
    valid_counts = [12, 0, 0, 816, 1781]
    test_counts = [58, 0, 0, 3167, 7319]
    figs.append(single_histogram(p_counts, "WN11 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN11"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN11"))

    # WN18
    # p_counts = [4, 0, 0, 0, 14]
    # all_counts = [34569, 0, 0, 0, 116873]
    # train_counts = [32232, 0, 0, 0, 109210]
    # valid_counts = [1165, 0, 0, 0, 3835]
    # test_counts = [1172, 0, 0, 0, 3828]
    p_counts = [0, 0, 0, 1, 17]
    all_counts = [0, 0, 0, 1396, 150046]
    train_counts = [0, 0, 0, 1299, 140143]
    valid_counts = [0, 0, 0, 41, 4959]
    test_counts = [0, 0, 0, 56, 4944]
    figs.append(single_histogram(p_counts, "WN18 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN18"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN18"))

    # WN18RR
    # p_counts = [11, 0, 0, 0, 0]
    # all_counts = [93003, 0, 0, 0, 0]
    # train_counts = [86835, 0, 0, 0, 0]
    # valid_counts = [3034, 0, 0, 0, 0]
    # test_counts = [3134, 0, 0, 0, 0]
    p_counts = [7, 0, 0, 1, 3]
    all_counts = [58434, 0, 0, 1396, 33173]
    train_counts = [54603, 0, 0, 1299, 30933]
    valid_counts = [1869, 0, 0, 41, 1124]
    test_counts = [1962, 0, 0, 56, 1116]
    figs.append(single_histogram(p_counts, "WN18RR p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "WN18RR"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "WN18RR"))

    # YAGO3-10
    # p_counts = [28, 5, 1, 2, 1]
    # all_counts = [302091, 16396, 66771, 379733, 324049]
    # train_counts = [299243, 16261, 66163, 376349, 321024]
    # valid_counts = [1395, 63, 311, 1698, 1533]
    # test_counts = [1453, 72, 297, 1686, 1492]
    p_counts = [23, 6, 1, 3, 4]
    all_counts = [164316, 116160, 66771, 381053, 360740]
    train_counts = [162747, 115088, 66163, 377651, 357391]
    valid_counts = [748, 544, 311, 1709, 1688]
    test_counts = [821, 528, 297, 1693, 1661]
    figs.append(single_histogram(p_counts, "YAGO3-10 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, "YAGO3-10"))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, "YAGO3-10"))

    # save all of the figures to one pdf

    with PdfPages('C:/Users/lklec/OneDrive/Desktop/Anomaly Papers/Histograms/output.pdf') as pdf:
        for fig in figs:
            pdf.savefig(fig, bbox_inches='tight')
    '''
    stacked_bar_chart()


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

    kw = dict(ha="center", va="center", fontsize=14, color="black")
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


def stacked_bar_chart():
    labels = ("[0.0-0.25)", "[0.25-0.50)", "[0.50-0.75)", "[0.75-1.00]")
    '''
    percents = {
        'p': [[2, 3, 0, 5], [0, 5, 10, 30], [0, 13, 10, 10], [0, 4, 5, 6], [20, 30, 0, 40], [10, 10, 0, 10], [2, 0, 5, 3]],
        'All': [[15, 5, 10, 30], [2, 3, 4, 5], [10, 13, 10, 10], [3, 4, 5, 6], [20, 30, 19, 40], [10, 10, 10, 10], [2, 3, 5, 3]],
        'Train': [[10, 13, 10, 10], [2, 3, 4, 5], [15, 5, 10, 30], [3, 4, 5, 6], [20, 30, 19, 40], [10, 10, 10, 10], [2, 3, 5, 3]],
        'Valid': [[20, 30, 19, 40], [2, 3, 4, 5], [15, 5, 10, 30], [10, 13, 10, 10], [3, 4, 5, 6], [10, 10, 10, 10], [2, 3, 5, 3]],
        'Test': [[10, 10, 10, 10], [2, 3, 5, 3], [2, 3, 4, 5], [15, 5, 10, 30], [10, 13, 10, 10], [3, 4, 5, 6], [20, 30, 19, 40]],
    }
    '''

    #Cart: 0.011, Cart: 0.007, N - Dup: 0.136, Cart: 0.003, N - Dup: 0.359, Cart: 0.500, N - Dup: 0.008, N - Dup: 0.272,
    #  Cart: 0.002, N - Rev: 0.932, N - Rev: 0.898, Sym: 0.921, N - Dup: 0.007]
    #p_counts = [7, 2, 1, 3]
    #           [[3, 2, 0, 0], [0, 0, 0, 2], [4, 0, 1, 0], [0, 0, 0, 0], [0, 0, 0, 1], [0, 0, 0, 0]]
    # access: [anomaly type][histogram bucket]
        # anomaly types: [near-duplicate, near-reverse, cartesian, transitive, symmetric, reflexive]
        # histogram buckets: ["[0.0-0.25)", "[0.25-0.50)", "[0.50-0.75)", "[0.75-1.00]"]
    percents = {
        'p': [[23.076923076923077, 15.384615384615385, 0.0, 0.0], [0.0, 0.0, 0.0, 15.384615384615385], [30.76923076923077, 0.0, 7.6923076923076925, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 7.6923076923076925], [0.0, 0.0, 0.0, 0.0]],
        'All': [[29.10085493808421, 19.299858618625912, 0.0, 0.0], [0.0, 0.0, 0.0, 3.558820723213434], [27.475981068195555, 0.0, 19.273837506830542, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.290647145050351], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[29.363884742847024, 21.108869437628073, 0.0, 0.0], [0.0, 0.0, 0.0, 3.892395456500291], [25.432277568367525, 0.0, 18.7909509474057, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.411621847251385], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [75.55856465809073, 0.0, 24.441435341909276, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [75.57830868411072, 0.0, 24.42169131588927, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]]
    }
    all_counts = [195684, 66753, 66663, 16773]
    train_counts = [173283, 66753, 59423, 16773]
    valid_counts = [4464, 0, 1444, 0]
    test_counts = [17937, 0, 5796, 0]
    x = np.arange(len(labels))
    width = 0.15
    multiplier = 0
    fig, ax = plt.subplots(layout='constrained')
    curr_color = 0
    #colors = ["silver", "purple", "#4dd2ff", "xkcd:orange", "yellowgreen", "pink"]
    colors = ["#003a7d", "#008dff", "#ff73b6", "#c701ff", "#4ecb8d", "#ff9d3a", "#f9e858", "#d83034"]
    #colors = ["grey", "purple", "cyan", "orange", "green", "hotpink"]
    hatch = ['xx', 'oo', '++', '..', '\\\\\\', '///', '--']
    #color_vary = [0.125, 0.28, 0.42, 0.57, 0.71, 0.86, 1.0]
    color_vary = [0.2, 0.4, 0.6, 0.8, 1.0]

    for attribute, measurement in percents.items():
        color_cnt = 0
        bottom = np.zeros(4)
        for msmt in measurement:
            offset = width * multiplier
            rects = ax.bar(x + offset, msmt, width, label=attribute, bottom=bottom, color=colors[color_cnt], linewidth=1, edgecolor="black") #hatch=hatch[color_cnt])#color_vary[curr_color]) )
            bottom += msmt
            color_cnt += 1
        ax.bar_label(rects, padding=3, fmt='{:.0f}', fontsize=8) #fmt='{:.0f}%', fontsize=7)
        multiplier += 1
        curr_color += 1

    ax.set_ylabel('Percent of data (%)')
    ax.set_xticks(x + width * 2, labels)

    x20 = []
    sec_labels = []
    for i in range(4):
        sec_labels.extend(["\n\np", "\n\nAll", "\n\nTrain", "\n\nValid", "\n\nTest"])
        for j in range(5):
            x20.append(j * width + i - 0.1)

    secax = ax.secondary_xaxis(location=0)
    secax.set_xticks(x20, sec_labels)
    secax.tick_params(axis='x', rotation=55, length=0)

    ax.margins(y=0.2)
    ax.set_xlabel('Anomaly coefficient value', labelpad=30.0)

    #ax.legend(loc='upper left', ncols=5)

    plt.show()


if __name__ == '__main__':
    main()
