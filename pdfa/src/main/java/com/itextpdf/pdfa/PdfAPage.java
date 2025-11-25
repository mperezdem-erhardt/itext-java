/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2025 Apryse Group NV
    Authors: Apryse Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itextpdf.pdfa;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.pdfa.checker.PdfAChecker;

/**
 * Represents a page in a PDF/A document.
 *
 * <p>
 * This class extends {@link PdfPage} to provide PDF/A-specific page handling,
 * particularly for managing page flushing in compliance with PDF/A standards.
 * It uses a {@link PdfAChecker} to verify that page objects are ready to be flushed
 * according to the applicable PDF/A conformance level.
 *
 * @see PdfPage
 * @see PdfADocument
 * @see PdfAChecker
 */
class PdfAPage extends PdfPage {
    /**
     * The PDF/A checker used to verify conformance before flushing page objects.
     */
    private final PdfAChecker checker;

    /**
     * Creates a new {@link PdfAPage} with the specified page size.
     *
     * @param pdfDocument the {@link PdfDocument} to which this page belongs
     * @param pageSize    the {@link PageSize} of the page
     * @param checker     the {@link PdfAChecker} used to verify PDF/A conformance
     */
    PdfAPage(PdfDocument pdfDocument, PageSize pageSize, PdfAChecker checker) {
        super(pdfDocument, pageSize);
        this.checker = checker;
    }

    /**
     * Creates a new {@link PdfAPage} from an existing page dictionary.
     *
     * @param pdfObject the {@link PdfDictionary} representing the page object
     * @param checker   the {@link PdfAChecker} used to verify PDF/A conformance
     */
    PdfAPage(PdfDictionary pdfObject, PdfAChecker checker) {
        super(pdfObject);
        this.checker = checker;
    }

    /**
     * Flushes the page dictionary, its content streams, annotations, and thumbnail image.
     *
     * <p>
     * This method overrides {@link PdfPage#flush(boolean)} to add PDF/A-specific behavior.
     * Before flushing, it verifies that the page is ready to be flushed according to PDF/A
     * conformance rules. The page will only be flushed if one of the following conditions is met:
     * <ul>
     *     <li>The {@code flushResourcesContentStreams} parameter is {@code true}</li>
     *     <li>The document is in the process of closing</li>
     *     <li>The PDF/A checker confirms that the page's PDF object is ready to be flushed</li>
     * </ul>
     *
     * <p>
     * This check is performed in advance to avoid processing actions that are invoked during
     * flushing (such as sending the END_PAGE event) if the page is not actually going to be flushed.
     *
     * @param flushResourcesContentStreams if {@code true}, all content streams rendered on this page
     *                                     (such as form XObjects, annotation appearance streams, and patterns)
     *                                     and all images associated with this page will also be flushed
     *
     * @see PdfPage#flush(boolean)
     * @see PdfAChecker#isPdfObjectReadyToFlush(PdfObject)
     */
    @Override
    public void flush(boolean flushResourcesContentStreams) {
        // We check in advance whether this PdfAPage can be flushed and call the flush method only if it is.
        // This avoids processing actions that are invoked during flushing (for example, sending the END_PAGE event)
        // if the page is not actually flushed.
        if (flushResourcesContentStreams || getDocument().isClosing() ||
                checker.isPdfObjectReadyToFlush(this.getPdfObject())) {

            super.flush(flushResourcesContentStreams);
        }
    }
}
