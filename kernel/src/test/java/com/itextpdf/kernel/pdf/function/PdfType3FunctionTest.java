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
package com.itextpdf.kernel.pdf.function;

import com.itextpdf.kernel.exceptions.KernelExceptionMessageConstant;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.test.ExtendedITextTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@Tag("UnitTest")
public class PdfType3FunctionTest extends ExtendedITextTest {
    private final static double EPSILON = 10e-6;

    @Test
    public void constructorNullFunctionsTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.remove(PdfName.Functions);
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_FUNCTIONS, ex.getMessage());
    }

    @Test
    public void constructorZeroSizeOfFunctionsTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.put(PdfName.Functions, new PdfArray());
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_FUNCTIONS, ex.getMessage());
    }

    @Test
    public void constructorDifferentOutputSizeOfFunctionsTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.Range, new PdfArray(new double[] {-100, 100, -100, 100}));
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_FUNCTIONS_OUTPUT, ex.getMessage());
    }

    @Test
    public void constructorDifferentOutputSizeFuncWithRangeTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.put(PdfName.Range, new PdfArray(new double[] {-100, 100, -100, 100}));
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_FUNCTIONS_OUTPUT, ex.getMessage());
    }

    @Test
    public void constructorInvalidInputSizeOfFuncTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        IPdfFunctionFactory customFactory = (dict) -> new CustomPdfFunction((PdfDictionary)dict, 2, 1);
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func, customFactory));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_FUNCTIONS_INPUT, ex.getMessage());
    }

    @Test
    public void constructorIgnoreNotDictFunctionsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).add(new PdfNumber(1));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);
        Assertions.assertEquals(2, type3Function.getFunctions().size());
    }

    @Test
    public void constructorInvalidFunctionTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.getAsArray(PdfName.Functions).getAsDictionary(0).remove(PdfName.N);
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_2_FUNCTION_N, ex.getMessage());
    }

    @Test
    public void constructorNullBoundsTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.remove(PdfName.Bounds);
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_BOUNDS, ex.getMessage());
    }

    @Test
    public void constructorInvalidSizeOfBoundsTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.put(PdfName.Bounds, new PdfArray());
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_BOUNDS, ex.getMessage());
    }

    @Test
    public void constructorInvalidBoundsLessThanDomainTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.getAsArray(PdfName.Bounds).remove(0);
        type3Func.getAsArray(PdfName.Bounds).add(new PdfNumber(-1));
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_BOUNDS, ex.getMessage());
    }

    @Test
    public void constructorInvalidBoundsMoreThanDomainTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.getAsArray(PdfName.Bounds).remove(0);
        type3Func.getAsArray(PdfName.Bounds).add(new PdfNumber(3));
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_BOUNDS, ex.getMessage());
    }

    @Test
    public void constructorInvalidBoundsLessThanPreviousTest() {
        PdfDictionary type3Func = new PdfDictionary();
        type3Func.put(PdfName.FunctionType, new PdfNumber(3));

        PdfArray domain = new PdfArray(new int[] {0, 1});
        type3Func.put(PdfName.Domain, domain);

        PdfArray functions = new PdfArray(PdfFunctionUtil.createMinimalPdfType2FunctionDict());
        functions.add(PdfFunctionUtil.createMinimalPdfType2FunctionDict());
        functions.add(PdfFunctionUtil.createMinimalPdfType2FunctionDict());
        type3Func.put(PdfName.Functions, functions);

        type3Func.put(PdfName.Bounds, new PdfArray(new double[] {1, 0.5}));

        type3Func.put(PdfName.Encode, new PdfArray(new double[] {0, 1, 0, 1, 0, 1}));

        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_BOUNDS, ex.getMessage());
    }

    @Test
    public void constructorNullEncodeTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.remove(PdfName.Encode);
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_ENCODE, ex.getMessage());
    }

    @Test
    public void constructorInvalidSizeOfEncodeTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.put(PdfName.Encode, new PdfArray());
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_NULL_ENCODE, ex.getMessage());
    }

    @Test
    public void constructorInvalidDomainTest() {
        PdfDictionary type3Func = createMinimalPdfType3FunctionDict();
        type3Func.put(PdfName.Domain, new PdfArray(new double[] {1}));
        Exception ex = Assertions.assertThrows(PdfException.class, () -> new PdfType3Function(type3Func));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_TYPE_3_FUNCTION_DOMAIN, ex.getMessage());
    }

    @Test
    public void getOutputSizeNullRangeTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        IPdfFunctionFactory customFactory = (dict) -> new CustomPdfFunction((PdfDictionary)dict, 1, 7);
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict, customFactory);
        Assertions.assertEquals(7, type3Function.getOutputSize());
    }

    @Test
    public void getEncodeTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0, 0.37, -1, 0}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        Assertions.assertArrayEquals(new double[] {0, 0.37, -1, 0}, type3Function.getEncode(), EPSILON);
    }

    @Test
    public void getBoundsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.put(PdfName.Bounds, new PdfArray(new double[] {0.789}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        Assertions.assertArrayEquals(new double[] {0.789}, type3Function.getBounds(), EPSILON);
    }

    @Test
    public void calculateInvalid2NumberInputTest() {
        PdfType3Function type3Func = new PdfType3Function(createMinimalPdfType3FunctionDict());

        Exception ex = Assertions.assertThrows(PdfException.class, () -> type3Func.calculate(new double[] {0, 1}));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_INPUT_FOR_TYPE_3_FUNCTION, ex.getMessage());
    }

    @Test
    public void calculateInvalidNullInputTest() {
        PdfType3Function type3Func = new PdfType3Function(createMinimalPdfType3FunctionDict());

        Exception ex = Assertions.assertThrows(PdfException.class, () -> type3Func.calculate(null));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_INPUT_FOR_TYPE_3_FUNCTION, ex.getMessage());
    }

    @Test
    public void calculateInputClipTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {-5});
        // input value was clipped to 0 from -5
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);
    }

    @Test
    public void calculateDomainOnePointIntervalTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.put(PdfName.Bounds, new PdfArray());
        type3FuncDict.getAsArray(PdfName.Functions).remove(1);
        type3FuncDict.put(PdfName.Domain, new PdfArray(new double[] {0.5, 0.5}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {7});
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);
    }

    @Test
    public void calculateInputClipByFuncTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.Domain, new PdfArray(new double[] {2, 3}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.1});
        // input value 0.1 was passed to first function with domain [2, 3], so value was clipped to 2 from 0.1
        Assertions.assertArrayEquals(new double[] {4}, output, EPSILON);
    }

    @Test
    public void calculateInputValueEqualBoundsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C0, new PdfArray(new double[] {-3}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.5});
        // Input value 0.5 was passed to second function.
        // Subdomain is [0.5, 1], encode is [0, 1], so value 0.5 was encoded to 0.
        Assertions.assertArrayEquals(new double[] {-3}, output, EPSILON);
    }

    @Test
    public void calculateInputValueNotEqualBoundsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.53});
        // Input value 0.53 was passed to second function.
        // Subdomain is [0.5, 1], encode is [0, 1], so value 0.53 was encoded to 0.06.
        Assertions.assertArrayEquals(new double[] {0.06}, output, EPSILON);
    }

    @Test
    public void calculateInputValueEqualDomainTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {1});
        Assertions.assertArrayEquals(new double[] {1}, output, EPSILON);
    }

    @Test
    public void calculateWith3FunctionsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();

        PdfDictionary minimalType2Func = PdfFunctionUtil.createMinimalPdfType2FunctionDict();
        minimalType2Func.put(PdfName.N, new PdfNumber(3));
        type3FuncDict.getAsArray(PdfName.Functions).add(1, minimalType2Func);
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0, 1, 0, 1, 0, 1}));
        type3FuncDict.put(PdfName.Bounds, new PdfArray(new double[] {0.5, 0.7}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.52});
        // Input value 0.52 was passed to second function.
        // Subdomain is [0.5, 0.7], encode is [0, 1], so value 0.52 was encoded to 0.1.
        Assertions.assertArrayEquals(new double[] {0.001}, output, EPSILON);
    }

    @Test
    public void calculateReverseEncodingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0, 1, 1, 0}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        Assertions.assertArrayEquals(new double[] {0, 1, 1, 0}, type3Function.getEncode(), EPSILON);

        double[] output = type3Function.calculate(new double[] {1});
        // Input value 1 was passed to second function.
        // Subdomain is [0.5, 1], encode is [1, 0], so value 1 was encoded to 0.
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);
    }

    @Test
    public void calculateOneFunctionTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.put(PdfName.Bounds, new PdfArray());
        type3FuncDict.getAsArray(PdfName.Functions).remove(1);
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.6});
        Assertions.assertArrayEquals(new double[] {0.36}, output, EPSILON);
    }

    @Test
    public void calculateBoundsEqualLeftDomainTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C0, new PdfArray(new double[] {-3}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C1, new PdfArray(new double[] {5}));
        type3FuncDict.put(PdfName.Bounds, new PdfArray(new double[] {0}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0});
        // first function was used
        Assertions.assertArrayEquals(new double[] {-3}, output, EPSILON);

        output = type3Function.calculate(new double[] {0.1});
        // second function was used
        Assertions.assertArrayEquals(new double[] {0.5}, output, EPSILON);

        output = type3Function.calculate(new double[] {1});
        // second function was used
        Assertions.assertArrayEquals(new double[] {5}, output, EPSILON);
    }

    @Test
    public void calculateBoundsEqualRightDomainTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C1, new PdfArray(new double[] {-3}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C0, new PdfArray(new double[] {5}));
        type3FuncDict.put(PdfName.Bounds, new PdfArray(new double[] {1}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0});
        // first function was used
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);

        output = type3Function.calculate(new double[] {0.1});
        // first function was used
        Assertions.assertArrayEquals(new double[] {-0.03}, output, EPSILON);

        output = type3Function.calculate(new double[] {1});
        // second function was used
        Assertions.assertArrayEquals(new double[] {5}, output, EPSILON);
    }

    @Test
    public void calculateBoundsEqualLeftDomainWith3FuncTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C0, new PdfArray(new double[] {-3}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C1, new PdfArray(new double[] {5}));
        PdfDictionary minimalType2Func = PdfFunctionUtil.createMinimalPdfType2FunctionDict();
        minimalType2Func.put(PdfName.N, new PdfNumber(1));
        minimalType2Func.put(PdfName.C1, new PdfArray(new double[] {-2}));
        type3FuncDict.getAsArray(PdfName.Functions).add(minimalType2Func);
        type3FuncDict.put(PdfName.Bounds, new PdfArray(new double[] {0, 0.5}));
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0, 1, 0, 1, 0, 1}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0});
        // first function was used
        Assertions.assertArrayEquals(new double[] {-3}, output, EPSILON);

        output = type3Function.calculate(new double[] {0.1});
        // second function was used
        Assertions.assertArrayEquals(new double[] {1}, output, EPSILON);

        output = type3Function.calculate(new double[] {0.6});
        // third function was used
        Assertions.assertArrayEquals(new double[] {-0.4}, output, EPSILON);

        output = type3Function.calculate(new double[] {1});
        // third function was used
        Assertions.assertArrayEquals(new double[] {-2}, output, EPSILON);
    }

    @Test
    public void calculateWithOutputClippingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        // Add Range to enable output clipping [0, 0.5]
        type3FuncDict.put(PdfName.Range, new PdfArray(new double[] {0, 0.5}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {1});
        // Input value 1 produces 1 from the function, but should be clipped to 0.5
        Assertions.assertArrayEquals(new double[] {0.5}, output, EPSILON);
    }

    @Test
    public void calculateWithNegativeEncodingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        // Use negative encoding values
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {-1, 0, -1, 0}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.25});
        // Input 0.25 in subdomain [0, 0.5] with encode [-1, 0] maps to -0.5
        // The function domain is [0, 1], so -0.5 is clipped to 0
        // Function with N=2: 0 + (1-0) * 0^2 = 0
        Assertions.assertArrayEquals(new double[] {0.0}, output, EPSILON);
    }

    @Test
    public void calculateMidpointInSubdomainTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.25});
        // Input 0.25 is exactly in the middle of subdomain [0, 0.5]
        // Mapped to 0.5 via encode [0, 1], and function calculates: 0.5^2 = 0.25
        Assertions.assertArrayEquals(new double[] {0.25}, output, EPSILON);
    }

    @Test
    public void calculateUpperBoundaryClippingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {10});
        // Input value 10 is beyond domain [0, 1], so it's clipped to 1
        // Then passed to second function with encode [0, 1], resulting in 1^1 = 1
        Assertions.assertArrayEquals(new double[] {1}, output, EPSILON);
    }

    @Test
    public void calculateFirstSubdomainMidpointTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C0, new PdfArray(new double[] {2}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C1, new PdfArray(new double[] {8}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.25});
        // Input 0.25 in subdomain [0, 0.5] with encode [0, 1] maps to 0.5
        // Function with C0=2, C1=8, N=2: 2 + (8-2) * 0.5^2 = 2 + 6 * 0.25 = 3.5
        Assertions.assertArrayEquals(new double[] {3.5}, output, EPSILON);
    }

    @Test
    public void calculateSecondSubdomainMidpointTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C0, new PdfArray(new double[] {10}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C1, new PdfArray(new double[] {20}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.75});
        // Input 0.75 in subdomain [0.5, 1] with encode [0, 1] maps to 0.5
        // Function with C0=10, C1=20, N=1: 10 + (20-10) * 0.5 = 15
        Assertions.assertArrayEquals(new double[] {15}, output, EPSILON);
    }

    @Test
    public void calculateWithNonUniformEncodingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        // Use non-uniform encoding that maps entire domain to partial function range
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0.2, 0.8, 0.3, 0.7}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.5});
        // Input 0.5 is at bound, maps to subdomain [0.5, 1] with encode [0.3, 0.7]
        // At x=0.5 (start of subdomain), encoded value is 0.3
        // Function with N=1: 0 + (1-0) * 0.3 = 0.3
        Assertions.assertArrayEquals(new double[] {0.3}, output, EPSILON);
    }

    @Test
    public void calculateEmptyArrayInputTest() {
        PdfType3Function type3Func = new PdfType3Function(createMinimalPdfType3FunctionDict());

        Exception ex = Assertions.assertThrows(PdfException.class, () -> type3Func.calculate(new double[] {}));
        Assertions.assertEquals(KernelExceptionMessageConstant.INVALID_INPUT_FOR_TYPE_3_FUNCTION, ex.getMessage());
    }

    @Test
    public void calculateZeroInputValueTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0});
        // Input 0 is at the left domain boundary, should use first function
        // With encode [0, 1], maps to 0, and function calculates: 0^2 = 0
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);
    }

    @Test
    public void calculateWithMultipleOutputComponentsTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        // Modify functions to have 2 output components
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.Range, 
            new PdfArray(new double[] {0, 1, 0, 1}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C0, 
            new PdfArray(new double[] {0, 0.5}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(0).put(PdfName.C1, 
            new PdfArray(new double[] {1, 1.5}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.Range, 
            new PdfArray(new double[] {0, 1, 0, 1}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C0, 
            new PdfArray(new double[] {0, 0.5}));
        type3FuncDict.getAsArray(PdfName.Functions).getAsDictionary(1).put(PdfName.C1, 
            new PdfArray(new double[] {1, 1.5}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.5});
        // Should return array with 2 components
        Assertions.assertEquals(2, output.length);
    }

    @Test
    public void calculateNearBoundaryValueTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        // Test value very close to but less than boundary
        double[] output = type3Function.calculate(new double[] {0.4999999});
        // Should use first function (subdomain [0, 0.5])
        // Input 0.4999999 maps to 0.9999998 via encode [0, 1]
        // Function with N=2: 0.9999998^2 ≈ 0.9999996
        Assertions.assertEquals(1, output.length);
        Assertions.assertTrue(output[0] > 0.99); // Very close to 1
    }

    @Test
    public void calculateWithScaledEncodingTest() {
        PdfDictionary type3FuncDict = createMinimalPdfType3FunctionDict();
        // Use encoding that scales the input range
        type3FuncDict.put(PdfName.Encode, new PdfArray(new double[] {0, 2, 0, 2}));
        PdfType3Function type3Function = new PdfType3Function(type3FuncDict);

        double[] output = type3Function.calculate(new double[] {0.5});
        // Input 0.5 at bound maps to subdomain [0.5, 1] with encode [0, 2]
        // At start of subdomain, encoded value is 0, which is within function domain [0, 1]
        // Function: 0^1 = 0
        Assertions.assertArrayEquals(new double[] {0}, output, EPSILON);
    }

    private static PdfDictionary createMinimalPdfType3FunctionDict() {
        PdfDictionary type3Func = new PdfDictionary();
        type3Func.put(PdfName.FunctionType, new PdfNumber(3));

        PdfArray domain = new PdfArray(new int[] {0, 1});
        type3Func.put(PdfName.Domain, domain);

        PdfArray functions = new PdfArray(PdfFunctionUtil.createMinimalPdfType2FunctionDict());
        PdfDictionary minimalType2Func = PdfFunctionUtil.createMinimalPdfType2FunctionDict();
        minimalType2Func.put(PdfName.N, new PdfNumber(1));
        functions.add(minimalType2Func);
        type3Func.put(PdfName.Functions, functions);

        type3Func.put(PdfName.Bounds, new PdfArray(new double[] {0.5}));

        type3Func.put(PdfName.Encode, new PdfArray(new double[] {0, 1, 0, 1}));

        return type3Func;
    }

    private static class CustomPdfFunction extends AbstractPdfFunction<PdfDictionary> {
        private final int inputSize;
        private final int outputSize;

        protected CustomPdfFunction(PdfDictionary pdfObject, int inputSize, int outputSize) {
            super(pdfObject);
            this.inputSize = inputSize;
            this.outputSize = outputSize;
        }

        @Override
        public int getInputSize() {
            return inputSize;
        }

        @Override
        public int getOutputSize() {
            return outputSize;
        }

        @Override
        public double[] calculate(double[] input) {
            return new double[0];
        }

        @Override
        protected boolean isWrappedObjectMustBeIndirect() {
            return false;
        }
    }
}
