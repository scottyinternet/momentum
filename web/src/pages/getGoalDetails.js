import MomentumClient from '../api/momentumClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


/**
 * Logic needed for the view playlist page of the website.
 */
class GetGoalDetails extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'getGoalDetails', 'displaySearchResults', 'getHTMLForHistory'], this);


        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        const openModalButton = document.getElementById('openModalBtn');
        const closeModalButton = document.getElementById('closeModalBtn');
        const modal = document.getElementById('new-entry-modal');
    
        openModalButton.addEventListener('click', () => {
            modal.style.display = 'block';
        });
    
        closeModalButton.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    
        // Close the modal if the user clicks outside of it
        window.addEventListener('click', (event) => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        this.header.addHeaderToPage();

        this.client = new MomentumClient();

        const urlParams = new URLSearchParams(window.location.search);
        const goalName = urlParams.get('goalName');

        this.getGoalDetails(goalName);

        const newEventForm = document.getElementById('myForm');
        newEventForm.addEventListener('submit', (e) => {
            e.preventDefault();
        
            // Get the values from the form fields
            var dateValue = document.getElementById('datePicker').value;
            var measurementValue = document.getElementById('measurement').value;
            this.client.createEvent(goalName, dateValue, measurementValue);

            modal.style.display = 'none';

        });
    }

    /**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async getGoalDetails(goalName) {


        if (goalName) {
            const results = await this.client.getGoalDetails(goalName);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: goalName,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY)


        const searchCriteriaDisplay = document.getElementById('search-criteria-display');
        const searchResultsDisplay = document.getElementById('search-results-display');
        const cont = document.getElementById('cont');
        const bottomContainer = document.getElementById('bottom-container');

        if (searchCriteria === '') {

            searchCriteriaDisplay.innerHTML = '';

        } else {

            searchCriteriaDisplay.innerHTML = `${searchCriteria}`;

            cont.innerHTML = `<div class="container">
                                 <div class="row">
                                     <div class="col-md-6">
                                        <!-- Content for the left column of the first row -->
                                        <div class="custom-bg" id="col1">
                                            <p>I'm Loading Stuff</p>
                                        </div>
                                    </div>
                                     <div class="col-md-6">
                                        <!-- Content for the left column of the first row -->
                                        <div class="custom-bg" id="col2">
                                            <p>I'm Loading Stuff</p>

                                        </div>
                                    </div>
                                 </div>
                             </div>`;
            var col1 = document.getElementById('col1')
            var col2 = document.getElementById('col2')
            col1.innerHTML = this.getHTMLForSearchResults(searchResults);
            col2.innerHTML = this.getHTMLForHistory(searchResults);

            var toggleButton = document.getElementById('view-events-button');
            function hideCol2() {
                col2.style.display = 'none';
            }
            
            // Function to show col2
            function showCol2() {
                col2.style.display = 'block';
            }
            
            // Initially hide col2
            hideCol2();
            
            // Toggle col2 when the button is clicked
            toggleButton.addEventListener('click', function() {
                if (col2.style.display === 'none') {
                    showCol2();
                } else {
                    hideCol2();
                }
            });

            
        }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of playlists objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        const goalCriteriaMessage = searchResults.currentGoalCriterion.goalCriteriaMessage;
        const statusString = searchResults.status.statusString;
        const streakMessage = searchResults.streakData.streakMessage;
        const sum = searchResults.status.sum;
        const statusMessage = searchResults.status.statusMessage;
        const unit = searchResults.currentGoalCriterion.units;

        const goalSummaryMessageHTML = document.getElementById("goal-summary-message");
        goalSummaryMessageHTML.textContent = goalCriteriaMessage;

        const urlParams = new URLSearchParams(window.location.search);
        const goalName2 = urlParams.get('goalName');

        // DIV
        const container = document.createElement('div');
        const cont = document.getElementById('col1');
        // STATUS
        const statusEnumElement = document.createElement('h4');
        statusEnumElement.textContent = `Status: ${statusString}`;
        container.appendChild(statusEnumElement);

        // CURRENT SUM
        const sumElement = document.createElement('p');
        sumElement.textContent = `Sum: ${sum} ${unit}`;
        container.appendChild(sumElement);

        // STATUS MESSAGE
        const statusMessageElement = document.createElement('p');
        statusMessageElement.textContent = `${statusMessage}`;
        container.appendChild(statusMessageElement);

        // CURRENT STREAK
        const currentStreakElement = document.createElement('h6');
        currentStreakElement.textContent = `Streak: ${streakMessage}`
        container.appendChild(currentStreakElement);


        //  L I S T   O F   E V E N T S
        const hr = document.createElement('hr');
        hr.style.borderTop = "2px solid gray"; 
        hr.style.paddingBottom = "30px"; 
        container.appendChild(hr);

        const eventSummaryList = Object.entries(searchResults.status.statusEventSummaries);

        const tableTitle = document.createElement('h4');
        tableTitle.textContent = 'Daily Event Summaries';
        container.appendChild(tableTitle);

        // TABLE
        const table = document.createElement('table');

        // TABLE - HEADER
        const tableHeader = table.createTHead();
        const headerRow = tableHeader.insertRow();

        const dateHeader = document.createElement('th');
        dateHeader.textContent = 'Date';
        headerRow.appendChild(dateHeader);

        const dailySumHeader = document.createElement('th');
        dailySumHeader.textContent = 'Daily Sum';
        headerRow.appendChild(dailySumHeader);

        // TABLE BODY
        const tableBody = table.createTBody();

        // TABLE BODY DATA
        eventSummaryList.forEach((eventSummary, index) => {
            const row = tableBody.insertRow();
            const dateCell = row.insertCell(0);
            dateCell.style.textAlign = "right";
            const measurementCell = row.insertCell(1);
            measurementCell.style.textAlign = "right";

            const dateArray = this.convertToDateArray(eventSummary[0]);
            const dayOfWeekStr = this.getDayOfWeek(dateArray);
            const formattedDate = this.formatDate(dateArray);
            console.log(dateArray + " - dateArray");
            console.log(formattedDate + "- formatted Date");

            dateCell.textContent = `${dayOfWeekStr}, ${formattedDate}`;
            if (eventSummary[1] === 0) {
                // Apply the "hide-zero" class to hide cells with a measurement of 0
                measurementCell.textContent = eventSummary[1];
                measurementCell.classList.add('hide-zero');
            } else {
                measurementCell.textContent = `${eventSummary[1]} ${unit}`;
            }

            // Add a CSS class to the last row to change its text color
            if (index === eventSummaryList.length - 1) {
                row.classList.add('last-row');
            }
        });

        container.appendChild(table);


        return container.outerHTML; // Return the container element html string
    }

    

    getHTMLForHistory(searchResults) {
        const container = document.createElement('div');


        //  S T R E A K   H I S T O R Y
        const currentStreak = searchResults.streakData.currentStreak;
        const longestStreak = searchResults.streakData.longestStreak;
        const totalDaysInMomentum = searchResults.streakData.totalDaysInMomentum;
        const percentString = searchResults.streakData.percentString;

        const streakTitle = document.createElement('h4');
        streakTitle.textContent = 'Streak History'
        container.appendChild(streakTitle);

        const currentStreakElement = document.createElement('p');
        currentStreakElement.textContent = `Current Streak: ${currentStreak} days`;
        container.appendChild(currentStreakElement);

        const longestStreakElement = document.createElement('p');
        longestStreakElement.textContent = `Longest Streak: ${longestStreak} days`;
        container.appendChild(longestStreakElement);

        const totalDaysInMomentumElement = document.createElement('p');
        totalDaysInMomentumElement.textContent = `Total Days in Moment: ${totalDaysInMomentum} days`;
        container.appendChild(totalDaysInMomentumElement);

        const percentStringElement = document.createElement('p');
        percentStringElement.textContent = `Percentage in Momentum: ${percentString} of days`;
        container.appendChild(percentStringElement);


        //  E N T R I E S   H I S T O R Y
        const hr = document.createElement('hr');
        hr.style.borderTop = "2px solid gray"; 
        hr.style.paddingBottom = "30px"; 
        container.appendChild(hr);

        const entryList = searchResults.eventEntries;
        const unit = searchResults.currentGoalCriterion.units;

        const tableTitle = document.createElement('h4');
        tableTitle.textContent = 'All Entries';
        container.appendChild(tableTitle);

        //  T A B L E
        const table = document.createElement('table');

        // TABLE - HEADER
        const tableHeader = table.createTHead();
        const headerRow = tableHeader.insertRow();

        const dateHeader = document.createElement('th');
        dateHeader.textContent = 'Date';
        headerRow.appendChild(dateHeader);

        const dailySumHeader = document.createElement('th');
        dailySumHeader.textContent = 'Measurement';
        headerRow.appendChild(dailySumHeader);

        // TABLE BODY
        const tableBody = table.createTBody();

        // TABLE BODY DATA
        entryList.forEach((event, index) => {
            const row = tableBody.insertRow();
            const dateCell = row.insertCell(0);
            dateCell.style.textAlign = "right";
            const measurementCell = row.insertCell(1);
            measurementCell.style.textAlign = "right";

            const dayOfWeekStr = this.getDayOfWeek(event.dateOfEvent);
            const formattedDate = this.formatDate(event.dateOfEvent);

            dateCell.textContent = `${dayOfWeekStr}, ${formattedDate}`;
            measurementCell.textContent = `${event.measurement} ${unit}`;
        });

        container.appendChild(table);


        //  D A I L Y   E V E N T   S U M M A R I E S
        const hr2 = document.createElement('hr');
        hr2.style.borderTop = "2px solid gray"; 
        hr2.style.paddingBottom = "30px"; 
        container.appendChild(hr2);

        const eventSummaryMap = Object.entries(searchResults.eventSummaryMap);
        const criteriaStatusContainerMap = Object.entries(searchResults.criteriaStatusContainerMap);

        const summaryHistory = document.createElement('h4');
        summaryHistory.textContent = 'Daily Summary History'
        container.appendChild(summaryHistory);
    
        //  T A B L E
        const tableSummaryHistory = document.createElement('table');

        // TABLE - HEADER
        const tableHeaderSummaryHistory = tableSummaryHistory.createTHead();
        const headerRowSummaryHistory = tableHeaderSummaryHistory.insertRow();

        const dateHeaderSummaryHistory = document.createElement('th');
        dateHeaderSummaryHistory.textContent = 'Date';
        headerRowSummaryHistory.appendChild(dateHeaderSummaryHistory);

        const dailySumHeaderSummaryHistory = document.createElement('th');
        dailySumHeaderSummaryHistory.textContent = 'Measurement';
        headerRowSummaryHistory.appendChild(dailySumHeaderSummaryHistory);

        const goalCriteriaHeaderSummaryHistory = document.createElement('th');
        goalCriteriaHeaderSummaryHistory.textContent = 'Target';
        headerRowSummaryHistory.appendChild(goalCriteriaHeaderSummaryHistory);

        const sumHeaderSummaryHistory = document.createElement('th');
        sumHeaderSummaryHistory.textContent = 'Sum';
        headerRowSummaryHistory.appendChild(sumHeaderSummaryHistory);

        const momentumBoolHeaderSummaryHistory =  document.createElement('th');
        momentumBoolHeaderSummaryHistory.textContent = 'In Momentum';
        headerRowSummaryHistory.appendChild(momentumBoolHeaderSummaryHistory);


        // TABLE BODY
        const tableBodySummaryHistory = tableSummaryHistory.createTBody();

        // TABLE BODY DATA
        eventSummaryMap.forEach((event, index) => {
            const critStatus = criteriaStatusContainerMap[index];

            const rowSH = tableBodySummaryHistory.insertRow();
            const dateCellSH = rowSH.insertCell(0);
            dateCellSH.style.textAlign = "right";
            const measurementCellSH = rowSH.insertCell(1);
            measurementCellSH.style.textAlign = "right";
            const critCellSH = rowSH.insertCell(2);
            critCellSH.style.textAlign = "right";
            const sumCellSH = rowSH.insertCell(3);
            sumCellSH.style.textAlign = "right";
            const momentumBoolCellSH = rowSH.insertCell(4);
            momentumBoolCellSH.style.textAlign = "right";

            const dateArray = this.convertToDateArray(event[0]);
            const formattedDate = this.formatDate(dateArray);

            dateCellSH.textContent = `${formattedDate}`;
            measurementCellSH.textContent = `${event[1]} ${unit}`;
            critCellSH.textContent = `${critStatus[1].goalCriteria.goalCriteriaMessage}`
            sumCellSH.textContent = `${critStatus[1].sumNMeasurements}`
            momentumBoolCellSH.textContent = `${critStatus[1].inMomentum}`

        });

        container.appendChild(tableSummaryHistory);

        return container.outerHTML; // Return the container element html string
    }

    


    convertToDateArray(dateString) {
        console.log(dateString + "!!!!!!!!!!!")
        const date = new Date(dateString);
    
        return [
            date.getFullYear(),
            date.getMonth() + 1,
            date.getDate() + 1
        ];
    }

    formatDate(dateArray) {
        const yearFormat = dateArray[0].toString().slice(2);
        return `${dateArray[1]}/${dateArray[2]}/${yearFormat}`;
    }
    getDayOfWeek(dateArray) {
        const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        const dateObject = new Date(dateArray[0], dateArray[1] - 1, dateArray[2]);
        const dayOfWeekNum = dateObject.getDay();
        return daysOfWeek[dayOfWeekNum];
    }
    
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const getGoalDetails = new GetGoalDetails();
    getGoalDetails.mount();
};

window.addEventListener('DOMContentLoaded', main);

/*

    "goalModel": {
        "goalInfo": {
            "goalName": "Cardio",
            "userId": "griffin.scott88@gmail.com",
            "goalId": "griffin.scott88@gmail.comCardio",
            "startDate": [
                2023,
                9,
                1
            ]
        },
        "goalCriteriaList": [
            {
                "target": 90,
                "timeFrame": 7,
                "units": "minutes",
                "effectiveDate": [
                    2023,
                    9,
                    1
                ],
                "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
            },
            {
                "target": 150,
                "timeFrame": 7,
                "units": "minutes",
                "effectiveDate": [
                    2023,
                    9,
                    29
                ],
                "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
            }
        ],
        "eventEntries": [
            {
                "goalId": "griffin.scott88@gmail.comCardio",
                "eventId": "testUUID003",
                "dateOfEvent": [
                    2023,
                    10,
                    1
                ],
                "measurement": 90.0
            },
            {
                "goalId": "griffin.scott88@gmail.comCardio",
                "eventId": "testUUID001",
                "dateOfEvent": [
                    2023,
                    10,
                    3
                ],
                "measurement": 60.0
            },
            {
                "goalId": "griffin.scott88@gmail.comCardio",
                "eventId": "testUUID002",
                "dateOfEvent": [
                    2023,
                    10,
                    3
                ],
                "measurement": 9.0
            }
        ],
        "currentGoalCriterion": {
            "target": 150,
            "timeFrame": 7,
            "units": "minutes",
            "effectiveDate": [
                2023,
                9,
                29
            ],
            "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
        },
        "eventSummaryMap": {
            "2023-11-07": 0.0,
            "2023-11-06": 0.0,
            "2023-11-05": 0.0,
            "2023-11-04": 0.0,
            "2023-11-03": 0.0,
            "2023-11-02": 0.0,
            "2023-11-01": 0.0,
            "2023-10-31": 0.0,
            "2023-10-30": 0.0,
            "2023-10-29": 0.0,
            "2023-10-28": 0.0,
            "2023-10-27": 0.0,
            "2023-10-26": 0.0,
            "2023-10-25": 0.0,
            "2023-10-24": 0.0,
            "2023-10-23": 0.0,
            "2023-10-22": 0.0,
            "2023-10-21": 0.0,
            "2023-10-20": 0.0,
            "2023-10-19": 0.0,
            "2023-10-18": 0.0,
            "2023-10-17": 0.0,
            "2023-10-16": 0.0,
            "2023-10-15": 0.0,
            "2023-10-14": 0.0,
            "2023-10-13": 0.0,
            "2023-10-12": 0.0,
            "2023-10-11": 0.0,
            "2023-10-10": 0.0,
            "2023-10-09": 0.0,
            "2023-10-08": 0.0,
            "2023-10-07": 0.0,
            "2023-10-06": 0.0,
            "2023-10-05": 0.0,
            "2023-10-04": 0.0,
            "2023-10-03": 69.0,
            "2023-10-02": 0.0,
            "2023-10-01": 90.0,
            "2023-09-30": 0.0,
            "2023-09-29": 0.0,
            "2023-09-28": 0.0,
            "2023-09-27": 0.0,
            "2023-09-26": 0.0,
            "2023-09-25": 0.0,
            "2023-09-24": 0.0,
            "2023-09-23": 0.0,
            "2023-09-22": 0.0,
            "2023-09-21": 0.0,
            "2023-09-20": 0.0,
            "2023-09-19": 0.0,
            "2023-09-18": 0.0,
            "2023-09-17": 0.0,
            "2023-09-16": 0.0,
            "2023-09-15": 0.0,
            "2023-09-14": 0.0,
            "2023-09-13": 0.0,
            "2023-09-12": 0.0,
            "2023-09-11": 0.0,
            "2023-09-10": 0.0,
            "2023-09-09": 0.0,
            "2023-09-08": 0.0,
            "2023-09-07": 0.0,
            "2023-09-06": 0.0,
            "2023-09-05": 0.0,
            "2023-09-04": 0.0,
            "2023-09-03": 0.0,
            "2023-09-02": 0.0,
            "2023-09-01": 0.0
        },
        "criteriaStatusContainerMap": {
            "2023-11-07": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-06": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-05": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-04": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-03": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-02": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-11-01": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-31": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-30": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-29": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-28": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-27": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-26": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-25": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-24": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-23": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-22": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-21": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-20": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-19": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-18": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-17": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-16": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-15": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-14": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-13": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-12": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-11": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-10": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-10-09": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 69.0,
                "inMomentum": false
            },
            "2023-10-08": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 69.0,
                "inMomentum": false
            },
            "2023-10-07": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 159.0,
                "inMomentum": true
            },
            "2023-10-06": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 159.0,
                "inMomentum": true
            },
            "2023-10-05": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 159.0,
                "inMomentum": true
            },
            "2023-10-04": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 159.0,
                "inMomentum": true
            },
            "2023-10-03": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 159.0,
                "inMomentum": true
            },
            "2023-10-02": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 90.0,
                "inMomentum": false
            },
            "2023-10-01": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 90.0,
                "inMomentum": false
            },
            "2023-09-30": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-29": {
                "goalCriteria": {
                    "target": 150,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        29
                    ],
                    "goalCriteriaMessage": "150 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-28": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-27": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-26": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-25": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-24": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-23": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-22": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-21": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-20": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-19": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-18": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-17": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-16": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-15": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-14": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-13": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-12": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-11": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-10": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-09": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-08": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-07": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-06": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-05": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-04": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-03": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-02": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            },
            "2023-09-01": {
                "goalCriteria": {
                    "target": 90,
                    "timeFrame": 7,
                    "units": "minutes",
                    "effectiveDate": [
                        2023,
                        9,
                        1
                    ],
                    "goalCriteriaMessage": "90 minutes in a 7 day rolling period"
                },
                "sumNMeasurements": 0.0,
                "inMomentum": false
            }
        },
        "status": {
            "statusEnum": "NO_MOMENTUM",
            "statusMessage": "The best time to plant a tree was 7 days ago. The second best time is today!",
            "sum": 0.0,
            "target": 150.0,
            "targetPercent": 0.0,
            "statusEventSummaries": {
                "2023-11-07": 0.0,
                "2023-11-06": 0.0,
                "2023-11-05": 0.0,
                "2023-11-04": 0.0,
                "2023-11-03": 0.0,
                "2023-11-02": 0.0,
                "2023-11-01": 0.0,
                "2023-10-31": 0.0
            }
        },
        "streakData": {
            "currentStreak": -31,
            "longestStreak": 5,
            "totalDaysInMomentum": 5,
            "totalDays": 68,
            "percentInMomentum": 0.07352941176470588,
            "streakMessage": "Last Streak ended 31 days ago.",
            "percentString": "7%"
        }
    }
}
*/

