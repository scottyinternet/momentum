import MusicPlaylistClient from '../api/musicPlaylistClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view playlist page of the website.
 */
class ViewPlaylist extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addPlaylistToPage', 'addSongsToPage', 'getGoalDetails'], this);
        this.dataStore = new DataStore();
//        this.dataStore.addChangeListener(this.addPlaylistToPage);
//        this.dataStore.addChangeListener(this.addSongsToPage);
        this.header = new Header(this.dataStore);
        //console.log("viewplaylist constructor");
    }

    /**
     * Once the client is loaded, get the playlist metadata and song list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const playlistId = urlParams.get('id');
//        document.getElementById('playlist-name').innerText = "Loading Playlist ...";
//        const playlist = await this.client.getPlaylist(playlistId);
//        this.dataStore.set('playlist', playlist);
//        document.getElementById('songs').innerText = "(loading songs...)";
//        const songs = await this.client.getPlaylistSongs(playlistId);
//        this.dataStore.set('songs', songs);
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        document.getElementById('add-song').addEventListener('click', this.getGoalDetails);

        this.header.addHeaderToPage();

        this.client = new MusicPlaylistClient();
        this.clientLoaded();
    }



    /**
     * When the playlist is updated in the datastore, update the playlist metadata on the page.
     */
    addPlaylistToPage() {
        const playlist = this.dataStore.get('playlist');
        if (playlist == null) {
            return;
        }

        document.getElementById('playlist-name').innerText = playlist.name;
        document.getElementById('playlist-owner').innerText = playlist.customerName;

        let tagHtml = '';
        let tag;
        for (tag of playlist.tags) {
            tagHtml += '<div class="tag">' + tag + '</div>';
        }
        document.getElementById('tags').innerHTML = tagHtml;
    }

    /**
     * When the songs are updated in the datastore, update the list of songs on the page.
     */
    addSongsToPage() {
        const songs = this.dataStore.get('songs')

        if (songs == null) {
            return;
        }

        let songHtml = '';
        let song;
        for (song of songs) {
            songHtml += `
                <li class="song">
                    <span class="title">${song.title}</span>
                    <span class="album">${song.album}</span>
                </li>
            `;
        }
        document.getElementById('songs').innerHTML = songHtml;
    }

    /**
     * Method to run when the add song playlist submit button is pressed. Call the MusicPlaylistService to add a song to the
     * playlist.
     */
    async getGoalDetails() {
        var element = document.getElementById("goal_name");
        element.innerHTML = "Cardio";
        var element2 = document.getElementById("status");
        element2.innerHTML = "In momentum";
        var element2 = document.getElementById("target");
        element2.innerHTML = "5";
        var element3 = document.getElementById("actual");
        element3.innerHTML = "2";
        var element4 = document.getElementById("event");
        element4.innerHTML = "Cardio";


    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const viewPlaylist = new ViewPlaylist();
    viewPlaylist.mount();
};

window.addEventListener('DOMContentLoaded', main);
