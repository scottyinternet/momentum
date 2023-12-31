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
            this.bindClassMethods(['mount', 'loadGoals', 'displayGoalSummary', 'addHTMLRowsToTable', 'submit'], this);
            this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
            this.header = new Header(this.dataStore);
            this.displayGoalSummary = this.displayGoalSummary.bind(this);


        }

        /**
         * Add the header to the page and load the MomentumClient.
         */
        mount() {
                const openModalButton = document.getElementById('openModalBtn');
                const closeModalButton = document.getElementById('closeModalBtn');
                const modal = document.getElementById('myModal');
            
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


            this.dataStore.addChangeListener(this.displayGoalSummary);

            this.header.addHeaderToPage();

            this.client = new MomentumClient();

            this.loadGoals();
            
            const newGoalForm = document.getElementById('new-goal-form');
            newGoalForm.addEventListener('submit', (e) => {
                e.preventDefault();                  
                
                // Get the values from the form fields
                var goalName = document.getElementById('goalName').value;
                var target = document.getElementById('target').value;
                var units = document.getElementById('units').value;
                var timePeriod = document.getElementById('timePeriod').value

                // Calculate N Days ago
                var today = new Date();
                var nDaysAgo = new Date();
                nDaysAgo.setDate(today.getDate() - (timePeriod+1)); 
                const year = nDaysAgo.getFullYear();
                const month = String(nDaysAgo.getMonth() + 1).padStart(2, '0'); // Ensure two digits for month
                const day = String(nDaysAgo.getDate()).padStart(2, '0'); // Ensure two digits for day
                const formattedDate = `${year}-${month}-${day}`;


                this.client.createGoal(goalName, formattedDate, target, timePeriod, units, formattedDate, (error) => {
                    console.log(error);
                });
    
                modal.style.display = 'none';
    
            });

        }

         /**
         * Method to run when the create playlist submit button is pressed. Call the MusicPlaylistService to create the
         * playlist.
         */
        async loadGoals() {
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
            const headers = ['Goal', 'Status', 'Message', 'Progress','',''];

            // Create a table row for headers
            const headerRow = document.createElement('tr');
            for (const headerText of headers) {
              const headerCell = document.createElement('th');
              headerCell.textContent = headerText;
              headerRow.appendChild(headerCell);
            }
          
            // Append the header row to the table
            goalSummaryTableHTML.appendChild(headerRow);

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
                if (goalSummary.currentStreak > 1) {
                    currentStreakCell.textContent = `Streak: ${goalSummary.currentStreak} Days`;
                } else if (goalSummary.currentStreak == 1) {
                    currentStreakCell.textContent = `Streak: 1 Day`;
                } else if (goalSummary.currentStreak < 0) {
                    if (goalSummary.percentOfTarget === `0.0`) {
                        currentStreakCell.textContent = `${-goalSummary.currentStreak} days since momentum`;
                    } else {
                        currentStreakCell.textContent = `Progress: ${goalSummary.percentOfTarget}%`;
                    }
                }       
                // currentStreakCell.textContent = `${goalSummary.currentStreak}`;
                row.appendChild(currentStreakCell);


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

                row.addEventListener('click', () => {
                    // Trigger the update action when the row is clicked
                    window.location.href = '/details.html?goalName=' + goalName;
                  });

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