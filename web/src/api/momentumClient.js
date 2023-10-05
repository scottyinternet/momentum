import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call Momentum.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */

export default class MomentumClient extends BindingClass {
    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'createEvent','createGoal','deleteEvent', 'getAllGoalsSummary', 'deleteGoal'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

     /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                 return undefined;
            }

             return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error)
        }
    }

    async login() {
        this.authenticator.login();
     }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
          throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    async createEvent(goalName, dateOfEvent, measurement, errorCallback){
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create Events.");
            const response = await this.axiosClient.post(`events`, {
                goalName: goalName,
                dateOfEvent: dateOfEvent,
                measurement: measurement
            }, {
                headers: {
                   Authorization: `Bearer ${token}`
                }
            });
            return response.data.event;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteEvent(goalId, eventId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can delete Events.");
            const response = await this.axiosClient.delete(`events/${goalId}/${eventId}`, {
                headers: {
                   Authorization: `Bearer ${token}`
            }}
            );
            return response.data.event;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createGoal(unit, goalName, target, timePeriod, errorCallback){
            try {
                const token = await this.getTokenOrThrow("Only authenticated users can create Goal.");
                const response = await this.axiosClient.post(`goals`, {
                    unit: unit,
                    timePeriod: timePeriod,
                    target: target,
                    goalName:goalName
                }, {
                    headers: {
                       Authorization: `Bearer ${token}`
                    }
                });
                return response.data.goal;
            } catch (error) {
                this.handleError(error)
            }
    }

    async updateGoal(goalName, target, timePeriod, errorCallback){
                try {
                    const token = await this.getTokenOrThrow("Only authenticated users can update Goal.");
                    const response = await this.axiosClient.put(`goals`, {
                        timePeriod: timePeriod,
                        target: target,
                        goalName:goalName
                    }, {
                        headers: {
                           Authorization: `Bearer ${token}`
                        }
                    });
                    return response.data.goal;
                } catch (error) {
                    this.handleError(error)
                }
        }
   
    async deleteGoal(goalName) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can delete Goals.");
            const response = await this.axiosClient.delete(`goals/${goalName}`, {
                headers: {
                   Authorization: `Bearer ${token}`
            }}
            );
            return response.data.goal;
        } catch (error) {
            this.handleError(error)
        }
    }

    async getGoalDetails(goalName) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can get goal details.");
            const response = await this.axiosClient.get(`goals/${goalName}`, {
                headers: {
                   Authorization: `Bearer ${token}`
            }}
            );
            return response.data.goalDetailsModel;
        } catch (error) {
            this.handleError(error)
        }
    }

        async getAllGoalsSummary(){
                try {
                    const token = await this.getTokenOrThrow("Only authenticated users can get Goal summaries.");
                    const response = await this.axiosClient.get(`goals`, {
                        headers: {
                           Authorization: `Bearer ${token}`
                        }}
                        );
                    return response.data.goalSummaryList;
                } catch (error) {
                    this.handleError(error)
                }

            }
      /**
       * Helper method to log the error and run any error functions.
       * @param error The error received from the server.
       * @param errorCallback (Optional) A function to execute if the call fails.
       */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
          console.error(errorFromApi)
          error.message = errorFromApi;
        }

        if (errorCallback) {
          errorCallback(error);
        }
    }
}