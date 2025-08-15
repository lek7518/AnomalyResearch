from matplotlib.backends.backend_pdf import PdfPages
import matplotlib.pyplot as plt
import numpy as np


def main():
    # FB15k-237
    dataset_name = "FB15k-237"
    p_counts = [82, 48, 28, 26, 53]
    all_counts = [125093, 74169, 28537, 20774, 61543]
    train_counts = [103522, 64631, 25542, 19555, 58865]
    valid_counts = [9973, 4363, 1384, 563, 1252]
    test_counts = [11598, 5175, 1611, 656, 1426]
    percents = {
        'p': [[8.016877637130804, 5.907172995780592, 5.907172995780592, 3.375527426160338], [5.48523206751055, 7.594936708860762, 5.0632911392405076, 4.641350210970465], [24.894514767932503, 5.0632911392405076, 2.109704641350211, 6.751054852320677], [1.2658227848101264, 1.2658227848101264, 0.0, 4.641350210970465], [0.0, 0.42194092827004215, 0.0, 5.48523206751055], [0.42194092827004215, 0.42194092827004215, 0.0, 1.2658227848101264]],
        'All': [[11.32156999316385, 4.378039185337098, 1.8744598795289504, 1.5745720956029359], [9.721846018909055, 11.964877658682559, 5.053592849127424, 2.7531633324304456], [26.209869855150963, 2.5919333410723726, 1.8025513033832503, 3.197513188613293], [0.20057010924944213, 2.9040746043416013, 0.0, 10.976860271640291], [0.0, 0.06513691650866127, 0.0, 2.774768151272427], [0.05836525687162223, 0.03643797804692438, 0.0, 0.5397980110668266]],
        'Train': [[10.872609007221211, 4.424232401741912, 2.053176046891939, 1.7742498575969718], [9.380960255774212, 12.276427245833561, 5.467173805192658, 3.088767616632673], [24.479723646252516, 2.4092754901420346, 1.6849493780203222, 3.016004262903552], [0.1863182845488121, 2.8616577549932933, 0.0, 12.25768516987303], [0.0, 0.06320856990610588, 0.0, 3.054958381566617], [0.05438876945409111, 0.03638167686456094, 0.0, 0.5578523785899344]],
        'Valid': [[14.342743085258055, 3.974907328200741, 0.58169375534645, 0.12546335899629313], [12.437981180496152, 9.347020245223836, 2.0986598232107214, 0.35357855717137154], [38.842315369261485, 3.9520958083832345, 2.6803535785571713, 4.61362988309096], [0.290846877673225, 3.176504134587967, 0.0, 1.710863986313088], [0.0, 0.09694895922440833, 0.0, 0.8098089535215284], [0.07413743940690048, 0.02281151981750784, 0.0, 0.46763615625891075]],
        'Test': [[14.702433304016422, 4.109254373106615, 0.6058829277826638, 0.16124303723248315], [11.927098602560344, 10.065474445421676, 2.086387178735463, 0.3469168376820092], [38.39050131926121, 3.8551744356493693, 2.61409166422359, 4.397537379067722], [0.31271376917814914, 3.23463304993648, 0.0, 1.886054920355712], [0.0, 0.06351998436431154, 0.0, 0.7329228965112868], [0.0977230528681716, 0.0488615264340858, 0.0, 0.36157529561223495]]
    }
    # single_histogram(p_counts, "FB15k-237 p Distribution")
    # anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name)
    # percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name)
    #stacked_bar_chart(percents, dataset_name)
    create_all_pdf()


def create_all_pdf():
    figs = []

    # BioKG
    dataset_name = "BioKG"
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
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    #figs.append(stacked_bar_chart(percents, dataset_name))

    # FB13
    dataset_name = "FB13"
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
    percents = {
        'p': [[23.076923076923077, 15.384615384615385, 0.0, 0.0], [0.0, 0.0, 0.0, 15.384615384615385], [30.76923076923077, 0.0, 7.6923076923076925, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 7.6923076923076925], [0.0, 0.0, 0.0, 0.0]],
        'All': [[29.10085493808421, 19.299858618625912, 0.0, 0.0], [0.0, 0.0, 0.0, 3.558820723213434], [27.475981068195555, 0.0, 19.273837506830542, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.290647145050351], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[29.363884742847024, 21.108869437628073, 0.0, 0.0], [0.0, 0.0, 0.0, 3.892395456500291], [25.432277568367525, 0.0, 18.7909509474057, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.411621847251385], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[26.167907921462422, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [49.39065673662831, 0.0, 24.441435341909276, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[26.32621244680403, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [49.2520962373067, 0.0, 24.42169131588927, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]]
    }
    figs.append(single_histogram(p_counts, "FB13 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # FB15k
    dataset_name = "FB15k"
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
    percents = {
        'p': [[0.9665427509293681, 1.1152416356877324, 1.4869888475836432, 20.07434944237923],
              [0.0, 0.0, 0.0, 66.61710037174684],
              [1.6356877323420076, 1.1895910780669146, 0.7434944237918216, 3.1970260223048297],
              [0.0, 0.0, 0.0, 0.446096654275093], [0.0, 0.0, 0.0, 2.3048327137546463],
              [0.0, 0.07434944237918216, 0.0, 0.14869888475836432]],
        'All': [[4.22989701340565, 1.228780860940236, 0.8527337292494425, 6.9491888898082195],
                [0.0, 0.0, 0.0, 74.19644621107592],
                [2.0984004066104593, 0.30090524861831797, 0.8708015528196782, 0.4255225738036819],
                [0.0, 0.0, 0.0, 0.22390592573955653], [0.0, 0.0, 0.0, 8.501670851534834],
                [0.0, 0.02026297970493724, 0.0, 0.10148375668889403]],
        'Train': [[4.228984439357373, 1.2298661677105278, 0.8575118702162099, 6.949716646451796],
                  [0.0, 0.0, 0.0, 74.23055747585617],
                  [2.103315381399257, 0.29494434348493814, 0.8753120200686341, 0.4276175534314958],
                  [0.0, 0.0, 0.0, 0.2274693568350506], [0.0, 0.0, 0.0, 8.453208373521656],
                  [0.0, 0.020904827152265795, 0.0, 0.10059154451486312]],
        'Valid': [[4.252000000000001, 1.1860000000000002, 0.8600000000000002, 7.015999999999987],
                  [0.0, 0.0, 0.0, 73.95999999999972],
                  [2.081999999999999, 0.33599999999999997, 0.8640000000000001, 0.43400000000000005],
                  [0.0, 0.0, 0.0, 0.206], [0.0, 0.0, 0.0, 8.662], [0.0, 0.01, 0.0, 0.132]],
        'Test': [[4.218652130487039, 1.25611552199895, 0.8075028355707538, 6.8883208342502975],
                 [0.0, 0.0, 0.0, 74.1175873101858],
                 [2.0720827478796693, 0.3199539537167138, 0.8396675187486246, 0.4012121006923872],
                 [0.0, 0.0, 0.0, 0.20991687968715614], [0.0, 0.0, 0.0, 8.76233684887678],
                 [0.0, 0.023700292867904725, 0.0, 0.08295102503766655]]
    }
    figs.append(single_histogram(p_counts, "FB15k p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # FB15k-237
    dataset_name = "FB15k-237"
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
    percents = {
        'p': [[8.016877637130804, 5.907172995780592, 5.907172995780592, 3.375527426160338],
              [5.48523206751055, 7.594936708860762, 5.0632911392405076, 4.641350210970465],
              [24.894514767932503, 5.0632911392405076, 2.109704641350211, 6.751054852320677],
              [1.2658227848101264, 1.2658227848101264, 0.0, 4.641350210970465],
              [0.0, 0.42194092827004215, 0.0, 5.48523206751055],
              [0.42194092827004215, 0.42194092827004215, 0.0, 1.2658227848101264]],
        'All': [[11.32156999316385, 4.378039185337098, 1.8744598795289504, 1.5745720956029359],
                [9.721846018909055, 11.964877658682559, 5.053592849127424, 2.7531633324304456],
                [26.209869855150963, 2.5919333410723726, 1.8025513033832503, 3.197513188613293],
                [0.20057010924944213, 2.9040746043416013, 0.0, 10.976860271640291],
                [0.0, 0.06513691650866127, 0.0, 2.774768151272427],
                [0.05836525687162223, 0.03643797804692438, 0.0, 0.5397980110668266]],
        'Train': [[10.872609007221211, 4.424232401741912, 2.053176046891939, 1.7742498575969718],
                  [9.380960255774212, 12.276427245833561, 5.467173805192658, 3.088767616632673],
                  [24.479723646252516, 2.4092754901420346, 1.6849493780203222, 3.016004262903552],
                  [0.1863182845488121, 2.8616577549932933, 0.0, 12.25768516987303],
                  [0.0, 0.06320856990610588, 0.0, 3.054958381566617],
                  [0.05438876945409111, 0.03638167686456094, 0.0, 0.5578523785899344]],
        'Valid': [[14.342743085258055, 3.974907328200741, 0.58169375534645, 0.12546335899629313],
                  [12.437981180496152, 9.347020245223836, 2.0986598232107214, 0.35357855717137154],
                  [38.842315369261485, 3.9520958083832345, 2.6803535785571713, 4.61362988309096],
                  [0.290846877673225, 3.176504134587967, 0.0, 1.710863986313088],
                  [0.0, 0.09694895922440833, 0.0, 0.8098089535215284],
                  [0.07413743940690048, 0.02281151981750784, 0.0, 0.46763615625891075]],
        'Test': [[14.702433304016422, 4.109254373106615, 0.6058829277826638, 0.16124303723248315],
                 [11.927098602560344, 10.065474445421676, 2.086387178735463, 0.3469168376820092],
                 [38.39050131926121, 3.8551744356493693, 2.61409166422359, 4.397537379067722],
                 [0.31271376917814914, 3.23463304993648, 0.0, 1.886054920355712],
                 [0.0, 0.06351998436431154, 0.0, 0.7329228965112868],
                 [0.0977230528681716, 0.0488615264340858, 0.0, 0.36157529561223495]]
    }
    figs.append(single_histogram(p_counts, "FB15k-237 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # Hetionet
    dataset_name = "Hetionet"
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
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    #figs.append(stacked_bar_chart(percents, dataset_name))

    # NELL-995
    dataset_name = "NELL-995"
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
    percents = {
        'p': [[12.0, 5.5, 6.0, 0.5], [7.0, 4.0, 2.0, 0.0], [42.5, 0.0, 0.0, 0.5], [4.5, 2.0, 2.0, 1.0], [1.5, 3.0, 3.5, 2.5], [0.0, 0.0, 0.0, 0.0]],
        'All': [[16.665261683515656, 3.3019265561269155, 3.486735878298198, 0.28661656280598913], [9.757932210643718, 6.719277881890632, 1.5400776847606883, 0.0], [37.06042940608119, 0.0, 0.0, 0.5200599171276092], [2.742310959517032, 1.4862560225143147, 2.6223470135461993, 3.7428751142899754], [0.3942598872987362, 3.1385162081017812, 3.7798369787242323, 2.7552800347571216], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[16.875559534467325, 2.8801827923943395, 3.4427237135718007, 0.29530057857534175], [10.053581688691725, 6.157217493552826, 1.586739534200083, 0.0], [36.885180186801016, 0.0, 0.0, 0.535816886917249], [2.8253985221609055, 1.5312871631101432, 2.701799863707425, 3.85627814374858], [0.4062053207552212, 3.2336081454856425, 3.894359892569382, 2.838760539291011], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[9.94475138121547, 14.917127071823206, 5.70902394106814, 0.0], [0.0, 27.07182320441989, 0.0, 0.0], [42.3572744014733, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[9.69438877755511, 17.53507014028056, 4.8346693386773545, 0.0], [0.0, 25.025050100200403, 0.0, 0.0], [42.91082164328657, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0]],
    }
    figs.append(single_histogram(p_counts, "NELL-995 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # WN11
    dataset_name = "WN11"
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
    percents = {
    'p': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 9.090909090909092, 72.72727272727273], [9.090909090909092, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 9.090909090909092], [0.0, 0.0, 0.0, 0.0]],
    'All': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 31.941241032656244, 65.79127364117899], [0.9297405634116468, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.3377447627531136], [0.0, 0.0, 0.0, 0.0]],
    'Train': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 32.13508496105027, 65.41512333342216], [0.9761860349437294, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 1.4736056705838463], [0.0, 0.0, 0.0, 0.0]],
    'Valid': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 31.276351092372558, 68.1870448447681], [0.45994633959371406, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.07665772326561901], [0.0, 0.0, 0.0, 0.0]],
    'Test': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 30.036039453717756, 69.21471927162368], [0.5500758725341426, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.19916540212443096], [0.0, 0.0, 0.0, 0.0]]
    }
    figs.append(single_histogram(p_counts, "WN11 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # WN18
    dataset_name = "WN18"
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
    percents = {
        'p': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 77.77777777777779], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0],
              [0.0, 0.0, 5.555555555555555, 16.666666666666664], [0.0, 0.0, 0.0, 0.0]],
        'All': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 77.17343933651166], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0],
                [0.0, 0.0, 0.9218050474769219, 21.90475561601141], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 77.2118606920151], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0],
                  [0.0, 0.0, 0.9183976470920943, 21.869741660892803], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 76.7], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0],
                  [0.0, 0.0, 0.8200000000000001, 22.48], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 76.55999999999997], [0.0, 0.0, 0.0, 0.0], [0.0, 0.0, 0.0, 0.0],
                 [0.0, 0.0, 1.1199999999999999, 22.32], [0.0, 0.0, 0.0, 0.0]],
    }
    figs.append(single_histogram(p_counts, "WN18 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # WN18RR
    dataset_name = "WN18RR"
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
    percents = {
        'p': [[18.181818181818183, 0.0, 0.0, 0.0], [9.090909090909092, 0.0, 0.0, 0.0],
              [27.272727272727273, 0.0, 0.0, 0.0], [9.090909090909092, 0.0, 0.0, 0.0],
              [0.0, 0.0, 9.090909090909092, 27.272727272727273], [0.0, 0.0, 0.0, 0.0]],
        'All': [[12.110362031332324, 0.0, 0.0, 0.0], [40.0212896358182, 0.0, 0.0, 0.0],
                [5.169725707772867, 0.0, 0.0, 0.0], [5.528853907938454, 0.0, 0.0, 0.0],
                [0.0, 0.0, 1.5010268485962819, 35.66874186854187], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[12.112627396787008, 0.0, 0.0, 0.0], [40.071399781194216, 0.0, 0.0, 0.0],
                  [5.151148730350665, 0.0, 0.0, 0.0], [5.546150745667069, 0.0, 0.0, 0.0],
                  [0.0, 0.0, 1.4959405769562963, 35.62273276904474], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[12.458800263678311, 0.0, 0.0, 0.0], [38.694792353328936, 0.0, 0.0, 0.0],
                  [5.372445616348055, 0.0, 0.0, 0.0], [5.075807514831905, 0.0, 0.0, 0.0],
                  [0.0, 0.0, 1.3513513513513513, 37.04680290046144], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[11.710274409700064, 0.0, 0.0, 0.0], [39.91703892788768, 0.0, 0.0, 0.0],
                 [5.488194001276324, 0.0, 0.0, 0.0], [5.488194001276324, 0.0, 0.0, 0.0],
                 [0.0, 0.0, 1.7868538608806637, 35.60944479897894], [0.0, 0.0, 0.0, 0.0]],
    }
    figs.append(single_histogram(p_counts, "WN18RR p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

    # YAGO3-10
    dataset_name = "YAGO3-10"
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
    percents = {
        'p': [[40.54054054054053, 2.7027027027027026, 0.0, 5.405405405405405], [5.405405405405405, 5.405405405405405, 2.7027027027027026, 0.0], [16.216216216216214, 0.0, 2.7027027027027026, 0.0], [2.7027027027027026, 5.405405405405405, 2.7027027027027026, 2.7027027027027026], [0.0, 0.0, 0.0, 5.405405405405405], [0.0, 0.0, 0.0, 0.0]],
        'All': [[11.960350400352603, 0.0017446558436788363, 0.0, 64.38634026298391], [0.15187688239183134, 0.9447770513479763, 0.2377323146991846, 0.0], [3.014398001909939, 0.0, 6.131179754646294, 0.0], [0.469312421949607, 9.211966502607801, 0.12120766913979283, 2.9694042459413796], [0.0, 0.0, 0.0, 0.39970983618599865], [0.0, 0.0, 0.0, 0.0]],
        'Train': [[11.953959074733095, 0.0017608244365361803, 0.0, 64.3912181198102], [0.15198695136417556, 0.9458407473309609, 0.23780397390272837, 0.0], [3.0152728351126927, 0.0, 6.1316540628707, 0.0], [0.4693060498220641, 9.21022390272835, 0.12066281138790036, 2.970696174377224], [0.0, 0.0, 0.0, 0.39961447212336887], [0.0, 0.0, 0.0, 0.0]],
        'Valid': [[12.0, 0.0, 0.0, 64.41999999999999], [0.08, 0.86, 0.2, 0.0], [2.8, 0.0, 6.22, 0.0], [0.45999999999999996, 9.64, 0.22, 2.6599999999999997], [0.0, 0.0, 0.0, 0.44], [0.0, 0.0, 0.0, 0.0]],
        'Test': [[13.3, 0.0, 0.0, 63.3], [0.2, 0.8, 0.26, 0.0], [3.04, 0.0, 5.94, 0.0], [0.48, 9.159999999999998, 0.13999999999999999, 3.0], [0.0, 0.0, 0.0, 0.37999999999999995], [0.0, 0.0, 0.0, 0.0]]
    }
    figs.append(single_histogram(p_counts, "YAGO3-10 p Distribution"))
    figs.append(anomaly_dist_histogram(all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(percent_comparison(p_counts, all_counts, train_counts, valid_counts, test_counts, dataset_name))
    figs.append(stacked_bar_chart(percents, dataset_name))

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


def stacked_bar_chart(percents, dataset_name):
    labels = ("[0.0-0.25)", "[0.25-0.50)", "[0.50-0.75)", "[0.75-1.00]")

    # access: [anomaly type][histogram bucket]
        # anomaly types: [near-duplicate, near-reverse, cartesian, transitive, symmetric, reflexive]
        # histogram buckets: ["[0.0-0.25)", "[0.25-0.50)", "[0.50-0.75)", "[0.75-1.00]"]
    '''
        percents = {
            'p': [[2, 3, 0, 5], [0, 5, 10, 30], [0, 13, 10, 10], [0, 4, 5, 6], [20, 30, 0, 40], [10, 10, 0, 10], [2, 0, 5, 3]],
            'All': [[15, 5, 10, 30], [2, 3, 4, 5], [10, 13, 10, 10], [3, 4, 5, 6], [20, 30, 19, 40], [10, 10, 10, 10], [2, 3, 5, 3]],
            'Train': [[10, 13, 10, 10], [2, 3, 4, 5], [15, 5, 10, 30], [3, 4, 5, 6], [20, 30, 19, 40], [10, 10, 10, 10], [2, 3, 5, 3]],
            'Valid': [[20, 30, 19, 40], [2, 3, 4, 5], [15, 5, 10, 30], [10, 13, 10, 10], [3, 4, 5, 6], [10, 10, 10, 10], [2, 3, 5, 3]],
            'Test': [[10, 10, 10, 10], [2, 3, 5, 3], [2, 3, 4, 5], [15, 5, 10, 30], [10, 13, 10, 10], [3, 4, 5, 6], [20, 30, 19, 40]],
        }
    '''

    x = np.arange(len(labels))
    width = 0.15
    multiplier = 0
    fig, ax = plt.subplots(layout='constrained')
    curr_color = 0
    colors = ["#003a7d", "#008dff", "#ff73b6", "#c701ff", "#4ecb8d", "#ff9d3a", "#f9e858", "#d83034"]
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
        #sec_labels.extend(["\n\np", "\n\nAll", "\n\nTrain", "\n\nValid", "\n\nTest"])
        sec_labels.extend(["p", "All", "Train", "Valid", "Test"])
        for j in range(5):
            x20.append(j * width + i - 0.01)# - 0.1)

    secax = ax.secondary_xaxis(location=0.115)
    secax.set_xticks(x20, sec_labels)
    secax.tick_params(axis='x', rotation=55, length=0)

    ax.use_sticky_edges = False
    ax.margins(y=0.15)
    ax.set_xlabel('Anomaly coefficient value')#, labelpad=30.0)
    ax.set_title(dataset_name + ': Distribution of anomalies by data split')

    #plt.show()
    return fig


if __name__ == '__main__':
    main()
