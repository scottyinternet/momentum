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

        this.bindClassMethods(['mount', 'getGoalDetails', 'displaySearchResults', 'getHTMLForSearchResults'], this);


        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        this.header.addHeaderToPage();

        this.client = new MomentumClient();

        const urlParams = new URLSearchParams(window.location.search);
        const goalName = urlParams.get('goalName');

        this.getGoalDetails(goalName);
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
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);


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
                                                    <p>Col1</p>
                                                </div>
                                                <div class="custom-bg" id="col1b">
                                                    <p>Col1b</p>
                                                </div>
                                     </div>
                                     <div class="col-md-6">
                                                 <!-- Content for the left column of the first row -->
                                                 <div class="custom-bg" id="col2">
                                                     <p>Col2</p>
                                                 </div>
                                        </div>
                                 </div>
                             </div>`;
            var col1 = document.getElementById('col1')
            var col1b = document.getElementById('col1b')
            var col2 = document.getElementById('col2')
            col1.innerHTML = this.getHTMLForSearchResults(searchResults);
            col1b.innerHTML = this.getListOfEventsToDisplay(searchResults);
            col2.innerHTML = this.getHTMLForAllEntries(searchResults);

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
        const goalSummaryMessage = searchResults.goalSummaryMessage;
        const statusString = searchResults.statusString;
        const sum = searchResults.status.sum;
        const statusMessage = searchResults.status.statusMessage;
        const unit = searchResults.unit;

        const goalSummaryMessageHTML = document.getElementById("goal-summary-message");
        goalSummaryMessageHTML.textContent = goalSummaryMessage;

        const urlParams = new URLSearchParams(window.location.search);
        const goalName2 = urlParams.get('goalName');
        const newEventButton = document.getElementById("create-event-button");
        newEventButton.href = `createEvent.html?goalName=${goalName2}&unit=${unit}`;

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


        return container.outerHTML; // Return the container element html string
    }

    getListOfEventsToDisplay(searchResults) {
        const eventSummaryList = searchResults.status.eventSummaryList;
        const unit = searchResults.unit;

        // DIV
        const container = document.createElement('div');

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


            const dayOfWeekStr = this.getDayOfWeek(eventSummary.date);
            const formattedDate = this.formateDate(eventSummary.date);

            dateCell.textContent = `${dayOfWeekStr}, ${formattedDate}`;
            if (eventSummary.summedMeasurement === 0) {
                // Apply the "hide-zero" class to hide cells with a measurement of 0
                measurementCell.textContent = eventSummary.summedMeasurement;
                measurementCell.classList.add('hide-zero');
            } else {
                measurementCell.textContent = `${eventSummary.summedMeasurement} ${unit}`;
            }

            // Add a CSS class to the last row to change its text color
            if (index === eventSummaryList.length - 1) {
                row.classList.add('last-row');
            }
        });

        container.appendChild(table);

        return container.outerHTML; // Return the container element html string
    }

    getHTMLForAllEntries(searchResults) {
        const entryList = searchResults.eventModelList;
        const unit = searchResults.unit;

        // DIV
        const container = document.createElement('div');
        const tableTitle = document.createElement('h4');
        tableTitle.textContent = 'All Entries';
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
            const formattedDate = this.formateDate(event.dateOfEvent);

            dateCell.textContent = `${dayOfWeekStr}, ${formattedDate}`;
            measurementCell.textContent = `${event.measurement} ${unit}`;
        });

        container.appendChild(table);

        return container.outerHTML; // Return the container element html string
    }


    formateDate(dateArray) {
        const yearFormat = dateArray[0].toString().slice(2);
        return `${dateArray[1]}/${dateArray[2]}/${yearFormat}`;
    }
    getDayOfWeek(dateArray) {
        const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thur', 'Fri', 'Sat'];
        const dateObject = new Date(dateArray);
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
{
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
                "eventId": "testUUID001",
                "dateOfEvent": [
                    2023,
                    10,
                    3
                ],
                "measurement": 60.0
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
            "2023-10-05": 0.0,
            "2023-10-04": 0.0,
            "2023-10-03": 60.0,
            "2023-10-02": 0.0,
            "2023-10-01": 0.0,
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
                "sumNMeasurements": 60.0,
                "inMomentumBool": false
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
                "sumNMeasurements": 60.0,
                "inMomentumBool": false
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
                "sumNMeasurements": 60.0,
                "inMomentumBool": false
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
                "sumNMeasurements": 0.0,
                "inMomentumBool": false
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
                "sumNMeasurements": 0.0,
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
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
                "inMomentumBool": false
            }
        },
        "status": {
            "statusEnum": "GAINING_MOMENTUM",
            "statusMessage": "Add 90 more minutes to be in momentum.",
            "sum": 60.0,
            "target": 150.0,
            "targetPercent": 40.0,
            "statusEventSummaries": {
                "2023-10-05": 0.0,
                "2023-10-04": 0.0,
                "2023-10-03": 60.0,
                "2023-10-02": 0.0,
                "2023-10-01": 0.0,
                "2023-09-30": 0.0,
                "2023-09-29": 0.0,
                "2023-09-28": 0.0
            }
        },
        "streakData": {
            "currentStreak": -35,
            "longestStreak": 0,
            "totalDaysInMomentum": 0,
            "totalDays": 35,
            "percentInMomentum": 0.0,
            "streakMessage": "Last Streak ended 35 days ago.",
            "percentString": "0%"
        }
    }
}
*/

