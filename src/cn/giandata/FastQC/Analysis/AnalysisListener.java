/**
 * Copyright Copyright 2010-17 Simon Andrews
 * <p>
 * This file is part of FastQC.
 * <p>
 * FastQC is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FastQC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with FastQC; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package cn.giandata.FastQC.Analysis;

import cn.giandata.FastQC.Modules.QCModule;
import cn.giandata.FastQC.Sequence.SequenceFile;

public interface AnalysisListener {

    void analysisStarted(SequenceFile file);

    void analysisUpdated(SequenceFile file, int sequencesProcessed, int percentComplete);

    void analysisComplete(SequenceFile file, QCModule[] results);

    void analysisExceptionReceived(SequenceFile file, Exception e);
}
