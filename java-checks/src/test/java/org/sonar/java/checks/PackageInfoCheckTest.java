/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks;

import java.io.File;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.java.CheckTestUtils.testSourcesPath;

class PackageInfoCheckTest {

  @Test
  void with_package_info() {
    PackageInfoCheck check = new PackageInfoCheck();
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/packageInfo/HelloWorld.java"))
      .withCheck(check)
      .verifyNoIssues();
    assertThat(check.directoriesWithoutPackageFile).isEmpty();
  }

  @Test
  void no_package_info() {
    PackageInfoCheck check = new PackageInfoCheck();
    String expectedMessage = "Add a 'package-info.java' file to document the '../java-checks-test-sources/src/main/java/checks/packageInfo/nopackageinfo' package"
      .replace('/', File.separatorChar);

    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/packageInfo/nopackageinfo/nopackageinfo.java"))
      .withCheck(check)
      .verifyIssueOnProject(expectedMessage);

    Set<File> set = check.directoriesWithoutPackageFile;
    assertThat(set).hasSize(1);
    assertThat(set.iterator().next()).hasName("nopackageinfo");

    // only one issue per package
    JavaCheckVerifier.newVerifier()
      .onFile(testSourcesPath("checks/packageInfo/nopackageinfo/HelloWorld.java"))
      .withCheck(check)
      .verifyNoIssues();
  }

}
