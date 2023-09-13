import MomentumClient from '../api/momentumClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create playlist page of the website.
 */
class CreateEvent extends BindingClass {
    goalName = '';
    unit = '';
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
        document.getElementById('create').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new MomentumClient();

        const urlParams = new URLSearchParams(window.location.search);
        this.goalName = urlParams.get('goalName');
        this.unit = urlParams.get('unit');
        const titleHTML = document.getElementById('title');
        titleHTML.innerHTML = `Create Event: ${this.goalName}`;
        const measurementLabel = document.getElementById("measurement-label");
        measurementLabel.innerHTML = `Measurement in ${this.unit}`;
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

        const createButton = document.getElementById('create');
        const origButtonText = createButton.innerText;

        const dateOfEvent = document.getElementById('date-of-event').value;
        const measurement = document.getElementById('measurement').value;

        const event = await this.client.createEvent(this.goalName, dateOfEvent, measurement,(error) => {
            createButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('event', event);
        window.location.href = `getGoalDetails.html?goalName=${this.goalName}`;       
    }
}

const main = async () => {
    const createEvent = new CreateEvent();
    createEvent.mount();
};

window.addEventListener('DOMContentLoaded', main);