/*--------------------------------------------------------------*/
/* Licensed to the Apache Software Foundation (ASF) under one   */
/* or more contributor license agreements.  See the NOTICE file */
/* distributed with this work for additional information        */
/* regarding copyright ownership.  The ASF licenses this file   */
/* to you under the Apache License, Version 2.0 (the            */
/* "License"); you may not use this file except in compliance   */
/* with the License.  You may obtain a copy of the License at   */
/*                                                              */
/*   http://www.apache.org/licenses/LICENSE-2.0                 */
/*                                                              */
/* Unless required by applicable law or agreed to in writing,   */
/* software distributed under the License is distributed on an  */
/* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       */
/* KIND, either express or implied.  See the License for the    */
/* specific language governing permissions and limitations      */
/* under the License.                                           */
/*--------------------------------------------------------------*/

package org.corehunter.ui.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.corehunter.data.CoreHunterData;

import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.corehunter.services.CoreHunterRunServices;
import org.corehunter.services.CoreHunterRunStatus;
import org.corehunter.services.DatasetServices;
import org.corehunter.services.simple.CoreHunterRunArgumentsPojo;
import org.corehunter.services.simple.CoreHunterRunPojo;
import org.jamesframework.core.subset.SubsetProblem;
import org.jamesframework.core.subset.SubsetSolution;
import org.joda.time.DateTime;

import uno.informatics.data.Dataset;
import uno.informatics.data.pojo.SimpleEntityPojo;
import org.corehunter.ui.Activator ;

public class CoreHunterRunServicesMock implements CoreHunterRunServices {

    private DatasetServices datasetServices;
    private ExecutorService executor;
    private List<CoreHunterRun> corehunterRuns;
    private Map<String, CoreHunterRunnable> corehunterRunnableMap;
    private String charsetName = "utf-8";
    private boolean shuttingDown;
    private boolean shutDown;
    private Random random;

    public CoreHunterRunServicesMock() {
        this.datasetServices = Activator.getDefault().getDatasetServices() ;

        executor = createExecutorService();

        corehunterRunnableMap = new HashMap<>();

        random = new Random();

        List<Dataset> datasets = datasetServices.getAllDatasets();

        Dataset dataset;

        for (int i = 0; i < datasets.size(); ++i) {

            dataset = datasets.get(i);
            
            if (dataset.getSize() > 0) {
                for (int j = 0; j < 4; ++j) {
    
                    executeCoreHunter(new CoreHunterRunArgumentsPojo(String.format("Run %s attempt %d", dataset.getName(), j + 1),
                            random.nextInt(datasets.size()), dataset.getUniqueIdentifier(), null));
                }
            }
        }
    }

    @Override
    public CoreHunterRun executeCoreHunter(CoreHunterRunArguments arguments) {

        if (shuttingDown) {
            throw new IllegalStateException("Can not accept any new runs, in the process of shutting down!");
        }

        if (shutDown) {
            throw new IllegalStateException("Can not accept any new runs, service is not running!");
        }

        CoreHunterRunnable corehunterRunnable = new CoreHunterRunnable(arguments);

        corehunterRunnableMap.put(corehunterRunnable.getUniqueIdentifier(), corehunterRunnable);

        executor.submit(corehunterRunnable);

        return new CoreHunterRunFromRunnable(corehunterRunnable);
    }

    @Override
    public CoreHunterRun getCoreHunterRun(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.remove(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return new CoreHunterRunFromRunnable(corehunterRunnable);
        } else {
            return null;
        }
    }

    @Override
    public boolean removeCoreHunterRun(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            boolean stopped = corehunterRunnable.stop();

            if (stopped) {
                corehunterRunnableMap.remove(uniqueIdentifier);

                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteCoreHunterRun(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.remove(uniqueIdentifier);

        if (corehunterRunnable != null) {
            corehunterRunnable.stop();
        }
    }

    @Override
    public List<CoreHunterRun> getAllCoreHunterRuns() {

        // iterates through all runnables can create new CoreHunterRun objects,
        // which will be a snapshot
        // of the current status of that runnable
        Iterator<CoreHunterRunnable> iterator = corehunterRunnableMap.values().iterator();

        corehunterRuns = new ArrayList<>(corehunterRunnableMap.size());

        while (iterator.hasNext()) {
            corehunterRuns.add(new CoreHunterRunFromRunnable(iterator.next()));
        }

        return corehunterRuns;
    }

    @Override
    public String getOutputStream(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return corehunterRunnable.getOutputStream();
        } else {
            return null;
        }
    }

    @Override
    public String getErrorStream(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return corehunterRunnable.getErrorStream();
        } else {
            return null;
        }
    }

    @Override
    public String getErrorMessage(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return corehunterRunnable.getErrorMessage();
        } else {
            return null;
        }
    }

    @Override
    public SubsetSolution getSubsetSolution(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return corehunterRunnable.getSubsetSolution();
        } else {
            return null;
        }
    }
    

    @Override
    public CoreHunterRunArguments getArguments(String uniqueIdentifier) {
        CoreHunterRunnable corehunterRunnable = corehunterRunnableMap.get(uniqueIdentifier);

        if (corehunterRunnable != null) {
            return corehunterRunnable.getArguments();
        } else {
            return null;
        }
    }

    public void shutdown() {
        if (!shuttingDown || shutDown) {
            shuttingDown = true;
            executor.shutdown();
            shutDown = true;
        }
    }

    private ExecutorService createExecutorService() {

        return Executors.newSingleThreadExecutor();
    }

    private String createUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }

    private class CoreHunterRunnable extends SimpleEntityPojo implements Runnable {
        private CoreHunterRunArguments coreHunterRunArguments;
        private ByteArrayOutputStream outputStream;
        private ByteArrayOutputStream errorStream;
        private String errorMessage;
        private SubsetSolution subsetSolution;
        private DateTime startDate;
        private DateTime endDate;
        private CoreHunterRunStatus status;
        private int type;
        private PrintWriter outputWriter;
        private PrintWriter errorWriter;

        public CoreHunterRunnable(CoreHunterRunArguments corehunterRunArguments) {
            super(createUniqueIdentifier(), corehunterRunArguments.getName());
            this.coreHunterRunArguments = new CoreHunterRunArgumentsPojo(corehunterRunArguments) ;

            status = CoreHunterRunStatus.NOT_STARTED;

            type = random.nextInt(4);

            outputStream = new ByteArrayOutputStream();
            outputWriter = new PrintWriter(outputStream);

            errorStream = new ByteArrayOutputStream();
            errorWriter = new PrintWriter(errorStream);
        }

        public final String getOutputStream() {
            try {
                return outputStream.toString(charsetName);
            } catch (UnsupportedEncodingException e) {
                return outputStream.toString();
            }
        }

        public final String getErrorStream() {
            try {
                return errorStream.toString(charsetName);
            } catch (UnsupportedEncodingException e) {
                return errorStream.toString();
            }
        }

        public synchronized final String getErrorMessage() {
            return errorMessage;
        }

        public synchronized final SubsetSolution getSubsetSolution() {
            return subsetSolution;
        }

        public synchronized final DateTime getStartDate() {
            return startDate;
        }

        public synchronized final DateTime getEndDate() {
            return endDate;
        }

        public CoreHunterRunStatus getStatus() {
            return status;
        }

        public final CoreHunterRunArguments getArguments() {
            return coreHunterRunArguments;
        }

        @Override

        public void run() {

            startDate = new DateTime();

            outputWriter.println(String.format("Run ID: %s ", coreHunterRunArguments.getUniqueIdentifier()));
            outputWriter.println(String.format("Run Name: %s ", coreHunterRunArguments.getName()));
            outputWriter.println(String.format("Desired subset size %d ", coreHunterRunArguments.getSubsetSize()));

            try {
                Dataset dataset = datasetServices.getDataset(coreHunterRunArguments.getDatasetId());

                if (dataset == null) {
                    throw new IllegalArgumentException(
                            String.format("Unknown Dataset ID: %s ", coreHunterRunArguments.getDatasetId()));
                }

                outputWriter.println(String.format("Dataset ID: %s ", dataset.getUniqueIdentifier()));
                outputWriter.println(String.format("Dataset Name: %s ", dataset.getName()));
                outputWriter.println(String.format("Dataset Abbreviation: %s ", dataset.getAbbreviation()));
                outputWriter.println(String.format("Dataset Description: %s ", dataset.getDescription()));
                outputWriter.println(String.format("Dataset size %d ", dataset.getSize()));

                CoreHunterData data = datasetServices.getCoreHunterData(coreHunterRunArguments.getDatasetId());

                if (data == null) {
                    throw new IllegalArgumentException(
                            String.format("Unknown data ID: %s ", coreHunterRunArguments.getDatasetId()));
                }

                outputWriter.println(String.format("Data ID: %s ", data.getUniqueIdentifier()));
                outputWriter.println(String.format("Data Name: %s ", data.getName()));
                outputWriter.println(String.format("Data size %d ", data.getSize()));

                SubsetProblem<CoreHunterData> problem = new SubsetProblem(data, null,
                        coreHunterRunArguments.getSubsetSize());

                subsetSolution = problem.createRandomSolution(random);

                switch (type) {
                    case 0:
                        subsetSolution = null;
                        runDelayStartFail();
                        break;
                    case 1:
                        subsetSolution = null;
                        runStartFail();
                        break;
                    case 2:
                        runDelayStartComplete();
                        break;
                    case 3:
                        runStartComplete();
                        break;
                }
            } catch (Exception e) {
                status = CoreHunterRunStatus.FAILED;
                errorMessage = e.getLocalizedMessage();

                e.printStackTrace(errorWriter);
            }

            endDate = new DateTime();

        }

        public boolean stop() {
            return false; // This simple implementation can not be stopped
        }

        public void runDelayStartFail() {

            int delay = random.nextInt(20) + 1;

            int i = 0;

            try {
                for (; i < delay; ++i) {

                    outputWriter.println(String.format("Waiting for %d seconds ", delay - i));
                    TimeUnit.SECONDS.sleep(1);
                }

                outputWriter.println("Stopped waiting");

                status = CoreHunterRunStatus.FAILED;
                errorMessage = "Failed!";

                outputWriter.println("Failed!");

            } catch (InterruptedException e) {
                status = CoreHunterRunStatus.FAILED;
                outputWriter.println(String.format("Interrupted at %d seconds ", i));
                errorMessage = e.getLocalizedMessage();

                e.printStackTrace(errorWriter);
            }
        }

        private void runStartFail() {

            outputWriter.println("Starting...");

            status = CoreHunterRunStatus.RUNNING;

            int runtime = random.nextInt(20) + 1;

            int i = 0;

            try {
                for (; i < runtime; ++i) {
                    outputWriter.println(String.format("Running. Step %d", i));

                    TimeUnit.SECONDS.sleep(1);
                }

                outputWriter.println("Failed!");

                status = CoreHunterRunStatus.FAILED;
                errorMessage = "Failed!";

                try {
                    throw new Exception("Failed!");
                } catch (Exception e) {
                    e.printStackTrace(errorWriter);
                }

            } catch (InterruptedException e) {
                status = CoreHunterRunStatus.FAILED;
                outputWriter.println(String.format("Interrupted at %d seconds ", i));
                errorMessage = e.getLocalizedMessage();

                e.printStackTrace(errorWriter);
            }
        }

        private void runDelayStartComplete() {
            int delay = random.nextInt(20) + 1;
            int runtime = random.nextInt(20) + 1;

            int i = 0;

            try {
                for (; i < delay; ++i) {

                    outputWriter.println(String.format("Waiting for %d seconds ", delay - i));
                    TimeUnit.SECONDS.sleep(1);
                }

                outputWriter.println("Stopped waiting");

                status = CoreHunterRunStatus.RUNNING;

                i = 0;

                for (; i < runtime; ++i) {
                    outputWriter.println(String.format("Running. Step %d", i));

                    TimeUnit.SECONDS.sleep(1);
                }

                status = CoreHunterRunStatus.FINISHED;

            } catch (InterruptedException e) {
                status = CoreHunterRunStatus.FAILED;
                outputWriter.println(String.format("Interrupted at %d seconds ", i));
                errorMessage = e.getLocalizedMessage();

                e.printStackTrace(errorWriter);
            }
        }

        private void runStartComplete() {
            int runtime = random.nextInt(20) + 1;

            int i = 0;

            try {
                status = CoreHunterRunStatus.RUNNING;

                for (; i < runtime; ++i) {
                    outputWriter.println(String.format("Running. Step %d", i));

                    TimeUnit.SECONDS.sleep(1);
                }

                status = CoreHunterRunStatus.FINISHED;

            } catch (InterruptedException e) {
                status = CoreHunterRunStatus.FAILED;
                outputWriter.println(String.format("Interrupted at %d seconds ", i));
                errorMessage = e.getLocalizedMessage();

                e.printStackTrace(errorWriter);
            }
        }
    }

    private class CoreHunterRunFromRunnable extends CoreHunterRunPojo {

        public CoreHunterRunFromRunnable(CoreHunterRunnable corehunterRunnable) {
            super(corehunterRunnable.getUniqueIdentifier(), corehunterRunnable.getName());

            setStartDate(corehunterRunnable.getStartDate());
            setEndDate(corehunterRunnable.getEndDate());
            setStatus(corehunterRunnable.getStatus());
        }
    }
}
