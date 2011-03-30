package org.sonatype.sisu.sitebricks;

import com.google.sitebricks.SitebricksModule;

public class SisuSitebricksModule
    extends SitebricksModule
{
    private Package packageToScanForServices;
    
    public SisuSitebricksModule( Package packageToScanForServices )
    {
        this.packageToScanForServices = packageToScanForServices;
    }
    
    @Override
    protected void configureSitebricks()
    {
        scan( packageToScanForServices );
    }
}