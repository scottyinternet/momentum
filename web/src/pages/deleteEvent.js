import MomentumClient from '../api/momentumClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the delete event page of the website.
 */
class DeleteEvent extends BindingClass {
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
        document.getElementById('delete').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new MomentumClient();
    }

     /**
     * Method to run when the Delete Event submit button is pressed. Call the momentum to delete the
     * event.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const deleteButton = document.getElementById('delete');
        const origButtonText = deleteButton.innerText;

        const goalId = document.getElementById('goal-id').value;
        const eventId = document.getElementById('event-id').value;

        const event = await this.client.deleteEvent(goalId,eventId,(error) => {
            deleteButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('event', event);
    }
}

const main = async () => {
    const deleteEvent = new DeleteEvent();
    deleteEvent.mount();
};

window.addEventListener('DOMContentLoaded', main);