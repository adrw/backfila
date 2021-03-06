import {
  MiskNavbarContainer,
  miskAdminDashboardTabsUrl,
  miskServiceMetadataUrl
} from "@misk/core"
import * as React from "react"
import { HomeLinkLogo } from "src/components"
import { backfilaTheme } from "src/utilities"

export const LayoutContainer = (props: { children: any }) => (
  <MiskNavbarContainer
    adminDashboardTabsUrl={miskAdminDashboardTabsUrl}
    children={props.children}
    homeName={<HomeLinkLogo />}
    menuShowButton={false}
    serviceMetadataUrl={miskServiceMetadataUrl}
    theme={backfilaTheme}
  />
)
