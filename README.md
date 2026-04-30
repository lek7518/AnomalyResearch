# AnomalyResearch

Results and implementations of rule-based redundancy measurements, IR metrics, and plots for *Assessing Difficulty of Link Prediction on Benchmarking Knowledge Graphs* by Lydia Klecan as partial fulfullment of the requirements for the degree of Master of Science at Rochester Institute of Technology.


## Organization


### MeasureAnomaly

* **App.java** - Parse datasets stored in text files into usable data structures. Find redundancies using these data structures in the main function.
* **Anomaly.java** - Implementations for measuring each type of redundancy.
* **Aggregate.java** - Implementations for aggregating redundancy measurements to create $\delta (p)$ and overall redundancy measurement.
* **Triple.java** - Definitions for Triple object and related comparator classes.

### AnalyzeRanks

* **App.java** - Parse rank data. Implementations for various IR metrics to aggregate rank data. 
* **Rank.java** - Definition for Rank object.

### Histograms

* **main.py** - Implementations for all histograms and plots.

### Results

* **Heatmaps** - All heatmap diagrams showing data for all models and datasets for one metric. Unweighted heatmaps show unweighted results. Weighted heatmaps show gain between unweighted and weigted results.
* __Redundancy*__ - Folders containing all redundancy measurements for various splits of the datasets. Each file pertains to one dataset.
* **All_Dataset_Info.xlsx** - Spreadsheet with redundancy measurements and triple counts for each predicate in each dataset. Not final measurements, these are found in Redundancy* folders.
* **Metric_Ranks_Table.txt** - LaTex tables containing all results for various metrics on all datasets. Contains unweighted, normalized, and weighted data.
* **NumTriples_**__*.text__ - Number of triples for each predicate in each dataset for various splits of the knowledge graph.
* **Overall_Redundancy_by_Type_**__*.png__ - Plots displaying the overall redundancy measurement for each dataset, split by type of redundancy, for various splits of the knowledge graph.

## Authors

Lydia Klecan 
([@Lydia-Klecan](http://www.linkedin.com/in/lydia-klecan))


## Acknowledgments

Thesis Advisor: Carlos R. Rivero