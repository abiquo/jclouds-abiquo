/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.abiquo.domain.builder;

/**
 * Base class for all builders that represent limits.
 * 
 * @author Ignasi Barrera
 */
public class LimitsBuilder
{
    /** The default limits for enterprises (unlimited). */
    protected static final int DEFAULT_LIMITS = 0;

    protected Integer ramSoftLimitInMb = DEFAULT_LIMITS;

    protected Integer ramHardLimitInMb = DEFAULT_LIMITS;

    protected Integer cpuCountSoftLimit = DEFAULT_LIMITS;

    protected Integer cpuCountHardLimit = DEFAULT_LIMITS;

    protected Long hdSoftLimitInMb = Long.valueOf(DEFAULT_LIMITS);

    protected Long hdHardLimitInMb = Long.valueOf(DEFAULT_LIMITS);

    protected Long storageSoft = Long.valueOf(DEFAULT_LIMITS);

    protected Long storageHard = Long.valueOf(DEFAULT_LIMITS);

    protected Long vlansSoft = Long.valueOf(DEFAULT_LIMITS);

    protected Long vlansHard = Long.valueOf(DEFAULT_LIMITS);

    protected Long publicIpsSoft = Long.valueOf(DEFAULT_LIMITS);

    protected Long publicIpsHard = Long.valueOf(DEFAULT_LIMITS);

    protected Long repositorySoft = Long.valueOf(DEFAULT_LIMITS);

    protected Long repositoryHard = Long.valueOf(DEFAULT_LIMITS);

    public LimitsBuilder ramLimits(final int soft, final int hard)
    {
        this.ramSoftLimitInMb = soft;
        this.ramHardLimitInMb = hard;
        return this;
    }

    public LimitsBuilder cpuCountLimits(final int soft, final int hard)
    {
        this.cpuCountSoftLimit = soft;
        this.cpuCountHardLimit = hard;
        return this;
    }

    public LimitsBuilder hdLimitsInMb(final long soft, final long hard)
    {
        this.hdSoftLimitInMb = soft;
        this.hdHardLimitInMb = hard;
        return this;
    }

    public LimitsBuilder storageLimits(final long soft, final long hard)
    {
        this.storageSoft = soft;
        this.storageHard = hard;
        return this;
    }

    public LimitsBuilder vlansLimits(final long soft, final long hard)
    {
        this.vlansSoft = soft;
        this.vlansHard = hard;
        return this;
    }

    public LimitsBuilder publicIpsLimits(final long soft, final long hard)
    {
        this.publicIpsSoft = soft;
        this.publicIpsHard = hard;
        return this;
    }

    public LimitsBuilder repositoryLimits(final long soft, final long hard)
    {
        this.repositorySoft = soft;
        this.repositoryHard = hard;
        return this;
    }
}
