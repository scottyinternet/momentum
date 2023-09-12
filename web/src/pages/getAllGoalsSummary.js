    import MomentumClient from '../api/momentumClient';
    import Header from '../components/header';
    import BindingClass from '../util/bindingClass';
    import DataStore from '../util/DataStore';

    const SEARCH_CRITERIA_KEY = 'search-criteria';
    const SEARCH_RESULTS_KEY = 'search-results';
    const EMPTY_DATASTORE_STATE = {
        [SEARCH_CRITERIA_KEY]: '',
        [SEARCH_RESULTS_KEY]: [],
    };

    /**
     * Logic needed for the create playlist page of the website.
     */
    class GetAllGoalsSummary extends BindingClass {
        constructor() {
            super();
            this.bindClassMethods(['mount', 'loadGoals', 'displayGoalSummary', 'getHTMLForAllGoalsSummary'], this);
            this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
            this.header = new Header(this.dataStore);
            this.displayGoalSummary = this.displayGoalSummary.bind(this);


        }

        /**
         * Add the header to the page and load the MomentumClient.
         */
        mount() {
            this.header.addHeaderToPage();

            this.client = new MomentumClient();

            this.loadGoals();
        }

         /**
         * Method to run when the create playlist submit button is pressed. Call the MusicPlaylistService to create the
         * playlist.
         */
        async loadGoals() {
                this.dataStore.addChangeListener(this.displayGoalSummary);
            const goalSummary = await this.client.getAllGoalsSummary();
            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: "goalSummaries",
                [SEARCH_RESULTS_KEY]: goalSummary
            });
        }

        displayGoalSummary() {
            const goalList = this.dataStore.get(SEARCH_RESULTS_KEY);

            const allGoalsContainer = document.getElementById('all-goals-container');
            const allGoalsDisplay = document.getElementById('all-goals-display');

            allGoalsDisplay.innerHTML = this.getHTMLForAllGoalsSummary(goalList);
        }

        getHTMLForAllGoalsSummary(goalList) {
            const summaryList = document.createElement('ul');

            for (const goal of goalList) {
                const listItem = document.createElement('li');
                listItem.textContent = `${goal.goalName} | ${goal.goalStatus}`;
                summaryList.appendChild(listItem);
            }
            return summaryList.outerHTML;
        }
    }

    const main = async () => {
        const getAllGoalsSummary = new GetAllGoalsSummary();
        getAllGoalsSummary.mount();
    };

    window.addEventListener('DOMContentLoaded', main);