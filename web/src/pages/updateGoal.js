import MomentumClient from '../api/momentumClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create playlist page of the website.
 */
class UpdateGoal extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }

    /**
     * Add the header to the page and load the MomentumClient.
     */
    mount() {
        document.getElementById('updateGoal').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new MomentumClient();
    }

     /**
     * Method to run when the create playlist submit button is pressed. Call the MusicPlaylistService to create the
     * playlist.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const createButton = document.getElementById('updateGoal');
        const origButtonText = createButton.innerText;



        const goalName = document.getElementById('goalName').value;
        const target = document.getElementById('target').value;
        const timePeriod = document.getElementById('timePeriod').value;


        const goal = await this.client.updateGoal(goalName,target,timePeriod ,(error) => {
            createButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('goal', goal);
    }
}

const main = async () => {
    const updateGoal = new UpdateGoal();
    updateGoal.mount();
};

window.addEventListener('DOMContentLoaded', main);