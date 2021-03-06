/**
 * Copyright Copyright 2014-17 Simon Andrews
 *
 *    This file is part of FastQC.
 *
 *    FastQC is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    FastQC is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with FastQC; if not, write to the Free Software
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package cn.giandata.FastQC.Modules;

public class ModuleFactory {

	public static QCModule [] getStandardModuleList () {

		OverRepresentedSeqs os = new OverRepresentedSeqs();
		
		QCModule [] module_list = new QCModule [] {
				new BasicStats(),
				new PerBaseQualityScores(),
				new PerTileQualityScores(),
				new PerSequenceQualityScores(),
				new PerBaseSequenceContent(),
				new PerSequenceGCContent(),
				new NContent(),
				new SequenceLengthDistribution(),
				new AdapterContent(),
				os.duplicationLevelModule(),
				os,
				new KmerContent(),
			};
	
		return (module_list);
	}
	
}
