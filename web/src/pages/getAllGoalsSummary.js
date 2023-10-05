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

            this.dataStore.addChangeListener(this.displayGoalSummary);

            this.header.addHeaderToPage();

            this.client = new MomentumClient();

            this.loadGoals();
        }

         /**
         * Method to run when the create playlist submit button is pressed. Call the MusicPlaylistService to create the
         * playlist.
         */
        async loadGoals() {
            console.log("inside loadGoals");
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
            goalSummaryTableHTML.innerHTML = '';
            this.addHTMLRowsToTable(goalList, goalSummaryTableHTML);
        }

        addHTMLRowsToTable(goalList, goalSummaryTableHTML) {

            for (const goalSummary of goalList) {
                const row = document.createElement('tr');

                const goalNameCell = document.createElement('td');
                const goalName = `${goalSummary.goalName}`;
                goalNameCell.textContent = goalName;
                row.appendChild(goalNameCell);

                const goalStatusCell = document.createElement('td');
                goalStatusCell.textContent = `${goalSummary.status}`;
                row.appendChild(goalStatusCell);

                const statusMessageCell = document.createElement('td');
                statusMessageCell.textContent = `${goalSummary.statusMessage}`;
                row.appendChild(statusMessageCell);

                const currentStreakCell = document.createElement('td');
                currentStreakCell.textContent = `${goalSummary.currentStreak}`;
                row.appendChild(currentStreakCell);


                const detailButtonCell = document.createElement('td');
                const detailsButton = document.createElement('button');

                detailsButton.textContent = 'Details';
                detailsButton.className = 'button';
                detailsButton.addEventListener('click', () => {
                    window.location.href = '/details.html?goalName=' + goalName;
                });
                detailButtonCell.appendChild(detailsButton);
                row.appendChild(detailButtonCell);

                const updateButtonCell = document.createElement('td');
                const updateButton = document.createElement('button');

                updateButton.textContent = 'Update';
                updateButton.className = 'button';
                updateButton.addEventListener('click', () => {
                    window.location.href = '/updateGoal.html?goalName=' + goalName;
                });
                updateButtonCell.appendChild(updateButton);
                row.appendChild(updateButtonCell);

                const deleteButtonCell = document.createElement('td');
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Delete';

                deleteButton.className = 'button';
                deleteButton.addEventListener('click', async () => {
                    let deleteYN = confirm("Are you sure? This will also delete all events related to this goal.");
                    if (deleteYN === true) {
                        await this.client.deleteGoal(goalName);
                        this.loadGoals();
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

            window.location.href='index.html';
        }

        toggleHide() {
            const form = document.getElementById("create-goal-form");
            if (form.style.display === "block") {
                form.style.display = "none";
            } else {
                form.style.display = "block";
            }
        }

    }


    const main = async () => {
        const getAllGoalsSummary = new GetAllGoalsSummary();
        getAllGoalsSummary.mount();
    };

    window.addEventListener('DOMContentLoaded', main);

    /*
    {
        "goalSummaryList": [
            {
                "goalName": "Cardio",
                "status": "Gaining Momentum",
                "statusMessage": "Add 90 more minutes to be in momentum.",
                "currentStreak": "-35"
            }
        ]
    }
    */ 