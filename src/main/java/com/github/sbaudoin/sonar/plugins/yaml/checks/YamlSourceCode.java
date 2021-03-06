/**
 * Copyright (c) 2018, Sylvain Baudoin
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
 */
package com.github.sbaudoin.sonar.plugins.yaml.checks;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import com.github.sbaudoin.yamllint.LintProblem;
import com.github.sbaudoin.yamllint.Linter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class to ease the working with files and associated issues
 */
public class YamlSourceCode {
    private static final Logger LOGGER = Loggers.get(YamlSourceCode.class);


    private final List<YamlIssue> yamlIssues = new ArrayList<>();

    private YamlIssue syntaxError = null;
    private InputFile yamlFile;


    /**
     * Constructor. Parses the passed file to determine if it is syntactically correct.
     *
     * @param yamlFile a supposedly YAML file
     * @throws IOException if there is a problem reading the passed file
     */
    public YamlSourceCode(InputFile yamlFile) throws IOException {
        this.yamlFile = yamlFile;

        LintProblem problem = Linter.getSyntaxError(getContent());
        LOGGER.debug("File {} has syntax error? {}", yamlFile.uri(), problem != null);
        if (problem != null) {
            syntaxError = new YamlLintIssue(problem, null, true);
        }
    }


    /**
     * Returns the {@code InputFile} of this class
     *
     * @return the {@code InputFile} of this class
     * @see InputFile
     */
    public InputFile getYamlFile() {
        return yamlFile;
    }

    /**
     * Returns the content of the YAML file as a {@code String}
     *
     * @return the YAML content
     */
    public String getContent() throws IOException {
        return yamlFile.contents();
    }

    /**
     * Adds an issue to list of issues already discovered
     *
     * @param issue an issue that relates to this YAML source code
     */
    public void addViolation(YamlIssue issue) {
        this.yamlIssues.add(issue);
        if (issue.isSyntaxError() && syntaxError == null) {
            syntaxError = issue;
        }
    }

    /**
     * Returns the syntax error if any found. May be {@code null}.
     * <p><strong>Warning!!!</strong> This issue has no rule key. It is up to the caller of this method to deal with the
     * rule key if it is required.</p>
     *
     * @return the syntax error if any
     * @see #hasCorrectSyntax()
     */
    public YamlIssue getSyntaxError() {
        return syntaxError;
    }
    /**
     * Returns {@code true} if succeeded or {@code false} if the file is corrupted (i.e. it contains
     * a syntax error you can get with {@link #getSyntaxError()})
     */
    public boolean hasCorrectSyntax() {
        return syntaxError == null;
    }

    /**
     * Returns the issues found on the source code
     *
     * @return the list of issues found on the source code. The returned list may be empty, whether because no issue has
     * been found, or the source code has not been parsed yet
     */
    public List<YamlIssue> getYamlIssues() {
        return yamlIssues;
    }
}
