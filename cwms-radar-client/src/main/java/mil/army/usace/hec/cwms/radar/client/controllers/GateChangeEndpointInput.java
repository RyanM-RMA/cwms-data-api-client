/*
 * Copyright (c) 2024. Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved. HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval from HEC
 */

package mil.army.usace.hec.cwms.radar.client.controllers;

import mil.army.usace.hec.cwms.http.client.EndpointInput;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;
import mil.army.usace.hec.cwms.radar.client.model.GateChange;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class GateChangeEndpointInput {

    private GateChangeEndpointInput() {
        throw new AssertionError("factory class");
    }

    public static GateChangeEndpointInput.GetAll getAll(String officeId, String projectId, Instant beginDate, Instant endDate) {
        return new GateChangeEndpointInput.GetAll(officeId, projectId, beginDate, endDate);
    }

    public static GateChangeEndpointInput.Post post(Set<GateChange> changes) {
        return new GateChangeEndpointInput.Post(changes);
    }

    public static GateChangeEndpointInput.Delete delete(String officeId, String projectId, Instant beginDate, Instant endDate) {
        return new GateChangeEndpointInput.Delete(officeId, projectId, beginDate, endDate);
    }


    public static final class GetAll extends EndpointInput {
        private static final String BEGIN = "begin";
        private static final String END = "end";
        private static final String START_INCLUSIVE = "start-time-inclusive";
        private static final String END_INCLUSIVE = "end-time-inclusive";
        private static final String UNIT_SYSTEM = "unit-system";
        private static final String PAGE_SIZE = "page-size";
        private final String officeId;
        private final String projectId;
        private final Instant beginDate;
        private final Instant endDate;
        private boolean startInclusive = true;
        private boolean endInclusive = true;
        private String unitSystem;
        private int pageSize;

        public GetAll(String officeId, String projectId, Instant beginDate, Instant endDate) {
            this.officeId = officeId;
            this.projectId = projectId;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        String officeId() {
            return officeId;
        }

        String projectId() {
            return projectId;
        }

        public GetAll startInclusive(boolean startInclusive) {
            this.startInclusive = startInclusive;
            return this;
        }

        public GetAll endInclusive(boolean endInclusive) {
            this.endInclusive = endInclusive;
            return this;
        }

        public GetAll unitSystem(String unitSystem) {
            this.unitSystem = unitSystem;
            return this;
        }

        public GetAll pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        @Override
        protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
            return httpRequestBuilder.addQueryParameter(BEGIN, DateTimeFormatter.ISO_INSTANT.format(beginDate))
                                     .addQueryParameter(END, DateTimeFormatter.ISO_INSTANT.format(endDate))
                                     .addQueryParameter(START_INCLUSIVE, Boolean.toString(startInclusive))
                                     .addQueryParameter(END_INCLUSIVE, Boolean.toString(endInclusive))
                                     .addQueryParameter(UNIT_SYSTEM, unitSystem)
                                     .addQueryParameter(PAGE_SIZE, Integer.toString(pageSize));
        }
    }

    public static final class Post extends EndpointInput {
        private static final String FAIL_IF_EXISTS = "fail-if-exists";
        private boolean failIfExists = true;
        private final Set<GateChange> changes = new HashSet<>();

        public Post(Set<GateChange> changes) {
            super();
            changes.addAll(Objects.requireNonNull(changes, "Set of changes is required to be non-null"));
        }

        public Post failIfExists(boolean failIfExists) {
            this.failIfExists = failIfExists;
            return this;
        }

        @Override
        protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
            return httpRequestBuilder.addQueryParameter(FAIL_IF_EXISTS, Boolean.toString(failIfExists));
        }

        Set<GateChange> gateChanges() {
            return changes;
        }
    }

    public static final class Delete extends EndpointInput {
        private static final String BEGIN = "begin";
        private static final String END = "end";
        private static final String OVERRIDE_PROTECTION = "override-protection";
        private final String officeId;
        private final String projectId;
        private final Instant beginDate;
        private final Instant endDate;
        private boolean overrideProtection = false;

        public Delete(String officeId, String projectId, Instant beginDate, Instant endDate) {
            this.officeId = officeId;
            this.projectId = projectId;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        String officeId() {
            return officeId;
        }

        String projectId() {
            return projectId;
        }

        public Delete overrideProtection(boolean overrideProtection) {
            this.overrideProtection = overrideProtection;
            return this;
        }

        @Override
        protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
            return httpRequestBuilder.addQueryParameter(BEGIN, beginDate.toString())
                                     .addQueryParameter(END, endDate.toString())
                                     .addQueryParameter(OVERRIDE_PROTECTION, Boolean.toString(overrideProtection));
        }
    }
}
