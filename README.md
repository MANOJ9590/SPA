# SPA

Dataset link:
 1. original Event link : https://data.4tu.nl/articles/dataset/BPI_Challenge_2018/12688355/1
 2. preprocessed or Size_reduced: https://drive.google.com/file/d/1UZe39NM6X3OGF3TFX7UV1UQ3Y5uTZXlk/view?usp=sharing
 3. Dataset used for visilization : https://drive.google.com/file/d/1i7hD7NziD5EhBM0FEhPHVDEF6yvtsmWd/view?usp=sharing

Python : SmartProcessAnalytics(Task2).ipynb

Upload the dataset 2 and 3 on your google drive and configure the file with the required permission(on default drive credentials should work on disk mount)

Input for the program 
1. Datasets in Xes format. 
2. Incomplete running Instance.
3. Number of desired Ngram.

The program requires the pm4py library for reading and visualsing the enent logs.

BPMN model of the cases with least events count 
![BPMN_Model.png](https://github.com/MANOJ9590/SPA/blob/main/BPMN_Model.png)

ProM PLugin - Java 

1. Import the project to java Ide via SVN checkout (check : https://svn.win.tue.nl/trac/prom/wiki/ManuallyCheckingOutProM)
2. for reading XES file OpenXES-20181205.jar is needed , add the jar file to the project 
3. Add files NgramPlugin.java and LogEventProcessor.java under the pacakge  org.processmining.plugins
4. Run the project as Prom with UITopia (running as UITopia needs a class loder in recent java verion)
5. add the Below VM argumnet to the build. (-Djava.system.class.loader=org.processmining.framework.util.ProMClassLoader)
6. Run the file NgramPlugin.java to launch ProM application.
