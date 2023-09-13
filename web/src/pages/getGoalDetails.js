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
        const cont=document.getElementById('cont');
        if (searchCriteria === '') {

            searchCriteriaDisplay.innerHTML = '';

        } else {

           searchCriteriaDisplay.innerHTML = `${searchCriteria}`;

            cont.innerHTML= `<div class="container">
                                 <div class="row">
                                     <div class="col-md-6">
                                                <!-- Content for the left column of the first row -->
                                                <div class="custom-bg" id="col1">
                                                    <p>Col1</p>
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
            var col1=document.getElementById('col1')
            var col2=document.getElementById('col2')
            col1.innerHTML =  this.getHTMLForSearchResults(searchResults);
            col2.innerHTML =  this.getListOfEventsToDisplay(searchResults);

        }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of playlists objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        const goalName = searchResults.goalName;
        const goalSummaryMessage = searchResults.goalSummaryMessage;
        const statusString = searchResults.statusString;
        const sum = searchResults.status.sum;
        const statusMessage = searchResults.status.statusMessage;
        const eventSummaryList = searchResults.status.eventSummaryList;
        const eventModelList = searchResults.eventModelList;
        const unit = searchResults.unit;

        const urlParams = new URLSearchParams(window.location.search);
        const goalName2 = urlParams.get('goalName');
        console.log("STUFF!");
        console.log(unit);
        console.log(goalName2);
        const newEventButton = document.getElementById("create-event-button");
        newEventButton.href = `createEvent.html?goalName=${goalName2}&unit=${unit}`;

       // DIV
       const container = document.createElement('div');
       const cont=document.getElementById('col1');
       // STATUS
       const statusEnumElement = document.createElement('h4');
       statusEnumElement.textContent = `Status: ${statusString}`;
       container.appendChild(statusEnumElement);


       // GOAL SUMMARY MESSAGE
       const summaryMessageElement = document.createElement('p');
       summaryMessageElement.textContent = `${goalSummaryMessage}`;
       container.appendChild(summaryMessageElement);

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

    getListOfEventsToDisplay(searchResults){

            const goalName = searchResults.goalName;
            const goalSummaryMessage = searchResults.goalSummaryMessage;
            const statusString = searchResults.statusString;
            const sum = searchResults.status.sum;
            const statusMessage = searchResults.status.statusMessage;
            const eventSummaryList = searchResults.status.eventSummaryList;
            const eventModelList = searchResults.eventModelList;
            const unit = searchResults.unit;

          // DIV
          const container = document.createElement('div');


           // TABLE
           const table = document.createElement('table');

           // TABLE - HEADER
           const tableHeader = table.createTHead();
           const headerRow = tableHeader.insertRow();
           const headers = ['Date', `Daily Sum`]; // Changed the header

           // TABLE HEADER DATA
           headers.forEach((headerText) => {
               const th = document.createElement('th');
               th.textContent = headerText;
               headerRow.appendChild(th);
           });

           // TABLE BODY
           const tableBody = table.createTBody();

           // TABLE BODY DATA
           eventSummaryList.forEach((eventSummary, index) => {
               const row = tableBody.insertRow();
               const dateCell = row.insertCell(0);
               const measurementCell = row.insertCell(1);

               // Extract the date and measurement from the event summary object
               const dateArray = eventSummary.date;
               const formattedDate = `${dateArray[0]}-${dateArray[1]}-${dateArray[2]}`;

               dateCell.textContent = formattedDate;
               measurementCell.textContent = eventSummary.summedMeasurement;
               if (eventSummary.summedMeasurement === 0) {
                // Apply the "hide-zero" class to hide cells with a measurement of 0
                    measurementCell.classList.add('hide-zero');
                }

               // Add a CSS class to the last row to change its text color
               if (index === eventSummaryList.length - 1) {
                   row.classList.add('last-row');
               }
           });

           container.appendChild(table);

           return container.outerHTML; // Return the container element html string





    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const getGoalDetails  = new GetGoalDetails();
    getGoalDetails.mount();
};

window.addEventListener('DOMContentLoaded', main);

