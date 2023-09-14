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
            this.bindClassMethods(['mount', 'loadGoals', 'displayGoalSummary', 'addHTMLRowsToTable', 'toggleHide', 'submit'], this);
            this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
            this.header = new Header(this.dataStore);
            this.displayGoalSummary = this.displayGoalSummary.bind(this);


        }

        /**
         * Add the header to the page and load the MomentumClient.
         */
        mount() {
            document.getElementById('createGoalForm').addEventListener('click', this.toggleHide);

            document.getElementById('createGoal').addEventListener('click', this.submit);

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


            const goalSummaryTableHTML = document.getElementById('goal-summaries-table');

            this.addHTMLRowsToTable(goalList, goalSummaryTableHTML);
        }

        addHTMLRowsToTable(goalList, goalSummaryTableHTML) {

            for (const goal of goalList) {
                const row = document.createElement('tr');

                const goalNameCell = document.createElement('td');
                const goalName = `${goal.goalName}`;
                goalNameCell.textContent = goalName;
                row.appendChild(goalNameCell);

                const goalStatusCell = document.createElement('td');
                goalStatusCell.textContent = `${goal.goalStatus}`;
                row.appendChild(goalStatusCell);

                const detailButtonCell = document.createElement('td');
                const detailsButton = document.createElement('button');

                detailsButton.textContent = 'Details';
                detailsButton.className = 'button';
                detailsButton.addEventListener('click', () => {
                    window.location.href = '/getGoalDetails.html?goalName=' + goalName;
                });
                detailButtonCell.appendChild(detailsButton);
                row.appendChild(detailButtonCell);

                const editButtonCell = document.createElement('td');
                const editButton = document.createElement('button');
                editButton.textContent = 'Edit';

                editButton.className = 'button';
                editButton.addEventListener('click', () => {
                    window.location.href = '/editGoal.html?goalName=' + goalName;
                });
                editButtonCell.appendChild(editButton);
                row.appendChild(editButtonCell);

                const deleteButtonCell = document.createElement('td');
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';

                deleteButton.className = 'button';
                deleteButton.addEventListener('click', () => {
                    const deleteYN = confirm("Are you sure? This will also delete all events related to this goal.");
                    if (deleteYN === true) {
                        const response = this.client.deleteGoal(goalName);
                        console.log("RESPONSE RECEIVED" + response);
                        // window.location.href='getAllGoalsSummary.html';
                    }
                });
                deleteButtonCell.appendChild(deleteButton);
                row.appendChild(deleteButtonCell);

                goalSummaryTableHTML.appendChild(row);
            }
        }

        async submit(evt) {
            evt.preventDefault();
    
            const createButton = document.getElementById('createGoal');
            const origButtonText = createButton.innerText;
    
            const unit = document.getElementById('units').value;
            const goalName = document.getElementById('goalName').value;
            const target = document.getElementById('target').value;
            const timePeriod = document.getElementById('timePeriod').value;
    
            const goal = await this.client.createGoal(unit,goalName,target,timePeriod);

            window.location.href='getAllGoalsSummary.html';
        }

        toggleHide() {
            const form = document.getElementById("create-goal-form");
            if (form.style.display === "none") {
                form.style.display = "block";
            } else {
                form.style.display = "none";
            }
        }

    }


    const main = async () => {
        const getAllGoalsSummary = new GetAllGoalsSummary();
        getAllGoalsSummary.mount();
    };

    window.addEventListener('DOMContentLoaded', main);