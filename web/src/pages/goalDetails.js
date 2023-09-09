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
class GoalDetails extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'getGoalDetails', 'displaySearchResults', 'getHTMLForSearchResults'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("goalIndex constructor");
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        document.getElementById('goalDetails-form').addEventListener('submit', this.getGoalDetails);
        document.getElementById('goalDetails-btn').addEventListener('click', this.getGoalDetails);

        this.header.addHeaderToPage();

        this.client = new MomentumClient();
    }

    /**
     * Uses the client to perform the search, 
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async getGoalDetails(evt) {
        alert("search button clicked");
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        if (searchCriteria) {
            const results = await this.client.getGoalDetails(searchCriteria);

            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
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

        const searchResultsContainer = document.getElementById('search-results-container');
        const searchCriteriaDisplay = document.getElementById('search-criteria-display');
        const searchResultsDisplay = document.getElementById('search-results-display');

        // if (searchCriteria === '') {
        //     searchResultsContainer.classList.add('hidden');
        //     searchCriteriaDisplay.innerHTML = '';
        //     searchResultsDisplay.innerHTML = '';
        // } else {
            searchResultsContainer.classList.remove('hidden');
            searchCriteriaDisplay.innerHTML = `"${searchCriteria}"`;
            searchResultsDisplay.innerHTML = this.getHTMLForSearchResults(searchResults);
        // }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of playlists objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        const eventSummaryList = searchResults.status.eventSummaryList;

        // Create a table element
        const table = document.createElement('table');
    
        // Create the table header row
        const tableHeader = table.createTHead();
        const headerRow = tableHeader.insertRow();
        const headers = ['Date', 'Summed Measurement'];
    
        // Populate the table header row with headers
        headers.forEach((headerText) => {
            const th = document.createElement('th');
            th.textContent = headerText;
            headerRow.appendChild(th);
        });
    
        // Create the table body
        const tableBody = table.createTBody();
    
        // Populate the table rows with event summary data
        eventSummaryList.forEach((eventSummary) => {
            const row = tableBody.insertRow();
            const dateCell = row.insertCell(0);
            const measurementCell = row.insertCell(1);
    
            // Extract the date and measurement from the event summary object
            const dateArray = eventSummary.date;
            const formattedDate = `${dateArray[0]}-${dateArray[1]}-${dateArray[2]}`;
    
            dateCell.textContent = formattedDate;
            measurementCell.textContent = eventSummary.summedMeasurement;
        });
    
        return table.outerHTML; // Return the table element
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const goalDetails = new GoalDetails();
    goalDetails.mount();
};

window.addEventListener('DOMContentLoaded', main);


