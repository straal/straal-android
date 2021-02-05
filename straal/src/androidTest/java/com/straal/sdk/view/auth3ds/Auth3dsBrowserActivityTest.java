/*
 * Auth3dsBrowserActivityTest.java
 * Created by Kamil Czanik on 03.02.2021
 * Straal SDK for Android
 * Copyright 2021 Straal Sp. z o. o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.straal.sdk.view.auth3ds;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.response.StraalEncrypted3dsResponse;
import com.straal.sdk.response.TransactionStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;

public class Auth3dsBrowserActivityTest {

    private final RedirectUrls redirectUrls = new RedirectUrls("com.daftmobile.straal//sdk.straal.com/x-callback-url/android/success", "com.daftmobile.straal//sdk.straal.com/x-callback-url/android/failure");
    private final String locationUrl = "https://domain.com/challenge_3ds";
    private final StraalEncrypted3dsResponse response = new StraalEncrypted3dsResponse("stub_id", redirectUrls, locationUrl, TransactionStatus.CHALLENGE_3DS);
    private ActivityScenario<Auth3dsBrowserActivity> scenario;

    @Before
    public void setUp() {
        init();
        intending(hasData(locationUrl)).respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        scenario = ActivityScenario.launch(Auth3dsBrowserActivity.startingIntent(context, response));
    }

    @Test
    public void testActivitySendsIntentWithLocationUrlOnStartup() {
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(locationUrl)));
    }

    @Test
    public void testActivitySendsIntentWithLocationUrlOnlyOnce() {
        scenario.recreate();
        scenario.recreate();
        scenario.recreate();
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(locationUrl)), times(1));
    }

    @Test
    public void testActivityFinishesWithResultSuccessWhenNewIntentWithSuccessUrlIsProvided() {
        deliverIntent(redirectUrls.successUrl);
        assertResult(Auth3dsBrowserActivity.AUTH_3DS_RESULT_SUCCESS);
    }

    @Test
    public void testActivityFinishesWithResultFailureWhenNewIntentWithFailureUrlIsProvided() {
        deliverIntent(redirectUrls.failureUrl);
        assertResult(Auth3dsBrowserActivity.AUTH_3DS_RESULT_FAILURE);
    }

    @Test
    public void testActivityFinishesWithResultSuccessWhenNewIntentWithSuccessUrlIsProvidedAfterBeingRecreated() {
        scenario.recreate();
        deliverIntent(redirectUrls.successUrl);
        assertResult(Auth3dsBrowserActivity.AUTH_3DS_RESULT_SUCCESS);
    }

    @Test
    public void testActivityFinishesWithResultFailureWhenNewIntentWithFailureUrlIsProvidedAfterBeingRecreated() {
        scenario.recreate();
        deliverIntent(redirectUrls.failureUrl);
        assertResult(Auth3dsBrowserActivity.AUTH_3DS_RESULT_FAILURE);
    }

    @Test(expected = Exception.class)
    public void testActivityThrowsExceptionWhenWaitingForResultAndNewIntentWithMalformedUrlIsProvided() {
        String malformedUrl = "com.daftmobile.straal//sdk.straal.com/malformed-callback-url/android/steal_money";
        deliverIntent(malformedUrl);
    }

    @Test(expected = Exception.class)
    public void testActivityThrowsExceptionWhenWaitingForResultAndNewIntentWithEmptyDataIsProvided() {
        scenario.onActivity(activity -> {
            activity.onNewIntent(new Intent());
        });
    }

    @Test
    public void testActivityFinishesWithResultCanceledOnBackPressed() {
        scenario.onActivity(Auth3dsBrowserActivity::onBackPressed);
        assertResult(Auth3dsBrowserActivity.AUTH_3DS_RESULT_CANCEL);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    private void assertResult(int auth3dsResultSuccess) {
        assertEquals(auth3dsResultSuccess, scenario.getResult().getResultCode());
    }

    private void deliverIntent(String data) {
        scenario.onActivity(activity -> {
            activity.onNewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
        });
    }

}
