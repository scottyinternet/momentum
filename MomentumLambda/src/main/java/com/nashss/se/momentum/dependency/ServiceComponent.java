package com.nashss.se.momentum.dependency;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.momentum.activity.*;

import com.nashss.se.momentum.activity.requests.UpdateGoalRequest;
import dagger.Component;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Music Playlist Service.
 */
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity
     * @return CreateGoalActivity
     */
    CreateGoalActivity provideCreateGoalActivity();

    /**
     * Provides the relevant activity.
     * @return CreateEventActivity
     */
    CreateEventActivity provideCreateEventActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteEventActivity
     */
    DeleteEventActivity provideDeleteEventActivity();

    /**
     * @return GetGoalDetailsActivity
     */
    GetGoalDetailsActivity provideGetGoalDetailsActivity();
    /**
     * @return UpdateGoalActivity
     */

    GetAllGoalsSummaryActivity provideGetAllGoalsSummaryActivity();

    UpdateGoalActivity provideUpdateGoalActivity();

    DeleteGoalActivity provideDeleteGoalActivity();
}
