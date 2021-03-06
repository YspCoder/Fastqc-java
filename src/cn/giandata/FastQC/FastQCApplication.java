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
package cn.giandata.FastQC;

import cn.giandata.FastQC.Analysis.OfflineRunner;

import javax.swing.*;
import java.io.IOException;


public class FastQCApplication extends JFrame {

    public static final String VERSION = "0.1";


    public static void main(String[] args) throws IOException {
        new OfflineRunner(args);
        System.exit(0);
    }

}
