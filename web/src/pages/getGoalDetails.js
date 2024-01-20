import MomentumClient from '../api/momentumClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view playlist page of the website.
 */
class GetGoalDetails extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'getGoalDetails', 'displaySearchResults', 'getHTMLForHistory'], this);

        this.goalName;
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        const urlParams = new URLSearchParams(window.location.search);
        this.goalName = urlParams.get('goalName');

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

        this.header.addHeaderToPage();

        this.client = new MomentumClient();

        this.getGoalDetails(this.goalName);

        const newEventForm = document.getElementById('myForm');
        newEventForm.addEventListener('submit', (e) => {
            e.preventDefault();
        
            // Get the values from the form fields
            var dateValue = document.getElementById('datePicker').value;
            var measurementValue = document.getElementById('measurement').value;
            this.client.createEvent(this.goalName, dateValue, measurementValue);

            modal.style.display = 'none';
        });
    }

    /**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async getGoalDetails(goalName) {
        console.log(" - - - - GET GOAL DETAILS - - - - ")
        const results = await this.client.getGoalDetails(goalName);
        console.log(results);
        this.dataStore.set(goalName, results);
        console.log(this.dataStore);
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchResults = this.dataStore.get(this.goalName);
        if(!searchResults) {
            return;
        }

        const searchCriteriaDisplay = document.getElementById('search-criteria-display');
        const searchResultsDisplay = document.getElementById('search-results-display');
        const cont = document.getElementById('cont');
        const bottomContainer = document.getElementById('bottom-container');

        if (this.goalName === '') {

            searchCriteriaDisplay.innerHTML = '';

        } else {

            searchCriteriaDisplay.innerHTML = `${this.goalName}`;

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
