package com.example;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ExamplePrinter implements YourExamplePrintHelper {

    private double mPrice;
    private double mQty;
    private boolean mIsSpecialStore;
    private Context context;
    private  StockAdjustmentType mMovementType;
    private  StockAdjustmentUpc mCurrentMovement;
    public WastePrinter(Context context,
                        StockAdjustmentType movementType,
                        StockAdjustmentUpc currentMovement,
                        double price, double qty, boolean isSpecialStore) {
        this.context = context;
        this.mIsSpecialStore = isSpecialStore;
        this.mQty = qty;
        this.mPrice = price;
        this.mMovementType = movementType;
        this.mCurrentMovement = currentMovement;


    }

    @Override
    public String generatePricePointLabel(int numberOfCopies) {
        Map<String, PrintLocalisationDetails> localisationDataMap = getPricePointLocalisationMap(mMovementType.getPrintLocalisationDetailsList());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document xmlDocument;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            xmlDocument = dBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("Root");
            Element templateElement = xmlDocument.createElement("Template");
            Element copiesElement = xmlDocument.createElement("Copies");
            Element nameElement = xmlDocument.createElement("Name");
            Element contentElement = xmlDocument.createElement("Content");
            Element reducedTextElement = xmlDocument.createElement("ReducedText");
            Element wasTextElement = xmlDocument.createElement("WasText");
            Element nowTextElement = xmlDocument.createElement("NowText");
            Element termsAndConditionsTitleElement = xmlDocument.createElement("TermsAndConditionsTitle");
            Element termsAndConditions1Element = xmlDocument.createElement("TermsAndConditions1");
            Element originalPriceElement = xmlDocument.createElement("OriginalPrice");
            Element reducedPriceElement1 = xmlDocument.createElement("ReducedPrice1");
            Element reducedPriceElement2 = xmlDocument.createElement("ReducedPrice2");
            Element looseWeightText = xmlDocument.createElement("LooseWeightText");

            if (null != localisationDataMap && localisationDataMap.size() > 0) {

                String firstVal = localisationDataMap.keySet().toArray()[0].toString();

                if (null != mCurrentMovement.getReductionCycle() && (mIsSpecialStore || mCurrentMovement.getReductionCycle().equals(ReductionCycle.SECOND)||mCurrentMovement.getReductionCycle().equals(ReductionCycle.THIRD))) {

                    PrintLocalisationDetails firstLocalisationItem = localisationDataMap.get(firstVal);

                    if (firstLocalisationItem.getCountryCode().equals("NLD")) {
                        if (mCurrentMovement.isLooseWeighed()) {
                            nameElement.setTextContent("WSPPS10");
                        } else {
                            nameElement.setTextContent("WSPPS9");
                        }
                    } else if (firstLocalisationItem.getCountryCode().equals("FRA") || firstLocalisationItem.getCountryCode().equals("BEL")) {
                        if (mCurrentMovement.isLooseWeighed()) {
                            nameElement.setTextContent("WSPPS12");
                        } else {
                            nameElement.setTextContent("WSPPS11");
                        }
                    } else {
                        if (mCurrentMovement.isLooseWeighed()) {
                            nameElement.setTextContent("WSPPS8");
                        } else {
                            nameElement.setTextContent("WSPPS7");
                        }
                    }

                } else {
                    if (localisationDataMap.size() > 0) {

                        PrintLocalisationDetails firstLocalisationItem = localisationDataMap.get(firstVal);

                        if (firstLocalisationItem.getCountryCode().equals("NLD")) {
                            if (mCurrentMovement.isLooseWeighed()) {
                                nameElement.setTextContent("WSPPS4");
                            } else {
                                nameElement.setTextContent("WSPPS3");
                            }
                        } else if (firstLocalisationItem.getCountryCode().equals("FRA") || firstLocalisationItem.getCountryCode().equals("BEL")) {
                            if (mCurrentMovement.isLooseWeighed()) {
                                nameElement.setTextContent("WSPPS6");
                            } else {
                                nameElement.setTextContent("WSPPS5");
                            }
                        } else {
                            if (mCurrentMovement.isLooseWeighed()) {
                                nameElement.setTextContent("WSPPS2");
                            } else {
                                nameElement.setTextContent("WSPPS1");
                            }
                        }
                    }
                }

                copiesElement.setTextContent(getNoOfCopies(numberOfCopies) + "");

                // append copies and name to template
                templateElement.appendChild(copiesElement);
                templateElement.appendChild(nameElement);

                String currencySymbol, perKg;
                double newPrice;

                currencySymbol = (localisationDataMap.get(PrintConstants.CURRENCY_SYMBOL)) != null ? localisationDataMap.get(PrintConstants.CURRENCY_SYMBOL).getRegionalText() : "";
                newPrice = mCurrentMovement.getUserEnteredPrice() == 0 ? mCurrentMovement.getRecommendedPrice() : mCurrentMovement.getUserEnteredPrice();

                // Added for Google Analytics
                mPrice = newPrice;
                mQty = mCurrentMovement.getQuantity();

                if (null != mCurrentMovement.getReductionCycle() && (mIsSpecialStore||mCurrentMovement.getReductionCycle().equals(ReductionCycle.SECOND)||mCurrentMovement.getReductionCycle().equals(ReductionCycle.THIRD))) {

                    reducedTextElement.setTextContent(localisationDataMap.get(PrintConstants.REDUCED_TEXT) != null ? localisationDataMap.get(PrintConstants.REDUCED_TEXT).getRegionalText() : "");
                    wasTextElement.setTextContent(localisationDataMap.get(PrintConstants.WAS_TEXT) != null ? localisationDataMap.get(PrintConstants.WAS_TEXT).getRegionalText() : "");
                    nowTextElement.setTextContent(localisationDataMap.get(PrintConstants.NOW_TEXT) != null ? localisationDataMap.get(PrintConstants.NOW_TEXT).getRegionalText() : "");
                    termsAndConditionsTitleElement.setTextContent(localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS_TITLE) != null ? localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS_TITLE).getRegionalText() : "");
                    termsAndConditions1Element.setTextContent(localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS1) != null ? localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS1).getRegionalText() : "");
                    perKg = (localisationDataMap.get(PrintConstants.PER_KG)) != null ? localisationDataMap.get(PrintConstants.PER_KG).getRegionalText() : "";

                    double weighedOriginalPrice, weighedNewPrice;

                    if (mCurrentMovement.isLooseWeighed()) {

                        if (mCurrentMovement.getQuantityUnit().contains(QuantityUnit.TRAY))// == "Tray"
                        {
                            weighedOriginalPrice = Double.parseDouble(mCurrentMovement.getPrice()) * mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize();
                            weighedNewPrice = (newPrice * mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize());
                            // Added for Google Analytics
                            mQty = mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize();

                        } else {
                            weighedOriginalPrice = Double.parseDouble(mCurrentMovement.getPrice()) * mCurrentMovement.getQuantity();
                            weighedNewPrice = (newPrice * mCurrentMovement.getQuantity());
                        }

                        originalPriceElement.setTextContent(currencySymbol + ' ' + String.format("%.2f", weighedOriginalPrice) + perKg);
                        reducedPriceElement1.setTextContent(currencySymbol + ' ' + String.format("%.2f", weighedNewPrice) + perKg);

                        looseWeightText.setTextContent(currencySymbol + ' ' + newPrice + ' ' + perKg);

                    } else {
                        originalPriceElement.setTextContent(currencySymbol + ' ' + String.format("%.2f", Double.parseDouble(mCurrentMovement.getPrice())));
                        reducedPriceElement1.setTextContent(currencySymbol + ' ' + String.format("%.2f", newPrice));
                    }
                    reducedPriceElement2.setTextContent(String.format("%.2f", newPrice));

                } else {

                    reducedTextElement.setTextContent(localisationDataMap.get(PrintConstants.REDUCED_TEXT) != null ? localisationDataMap.get(PrintConstants.REDUCED_TEXT).getRegionalText() : "");
                    wasTextElement.setTextContent(localisationDataMap.get(PrintConstants.NOW_TEXT) != null ? localisationDataMap.get(PrintConstants.NOW_TEXT).getRegionalText() : "");
                    nowTextElement.setTextContent(" ");

                    String conditions2 = localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS2) != null ? localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS2).getRegionalText() : "";
                    String condition3 = localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS3) != null ? localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS3).getRegionalText() : "";

                    termsAndConditionsTitleElement.setTextContent(conditions2.concat(condition3));
                    termsAndConditions1Element.setTextContent(localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS4) != null ? localisationDataMap.get(PrintConstants.TERMS_AND_CONDITIONS4).getRegionalText() : "");
                    perKg = (localisationDataMap.get(PrintConstants.PER_KG)) != null ? localisationDataMap.get(PrintConstants.PER_KG).getRegionalText() : "";

                    //Modified code to handle loose weighed products
                    //Total value should be printed in label for loose weighed, instead of price per unit
                    double weighedOriginalPrice, weighedNewPrice;

                    if (mCurrentMovement.isLooseWeighed()) {

                        if (mCurrentMovement.getQuantityUnit().contains(QuantityUnit.TRAY))// == "Tray")
                        {
                            weighedOriginalPrice = Double.parseDouble(mCurrentMovement.getPrice()) * mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize();
                            weighedNewPrice = (newPrice * mCurrentMovement.getQuantity()) * mCurrentMovement.getUnitSize();
                            // Added for Google Analytics
                            mQty = mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize();

                        } else {
                            weighedOriginalPrice = mCurrentMovement.getQuantity() * mCurrentMovement.getUnitSize();
                            weighedNewPrice = (newPrice * mCurrentMovement.getQuantity());
                        }

                        originalPriceElement.setTextContent(currencySymbol + ' ' + String.format("%.2f", weighedNewPrice));
                        reducedPriceElement1.setTextContent(currencySymbol + ' ' + String.format("%.2f", newPrice) + perKg);

                        looseWeightText.setTextContent(currencySymbol + ' ' + String.format("%.2f", newPrice) + ' ' + perKg);

                    } else {
                        originalPriceElement.setTextContent(currencySymbol + ' ' + String.format("%.2f", newPrice));

                        reducedPriceElement1.setTextContent(" ");
                    }
                    reducedPriceElement2.setTextContent(String.format("%.2f", newPrice));

                }

            }

            contentElement.appendChild(reducedTextElement);
            contentElement.appendChild(wasTextElement);
            contentElement.appendChild(originalPriceElement);
            contentElement.appendChild(nowTextElement);
            contentElement.appendChild(reducedPriceElement1);
            contentElement.appendChild(termsAndConditionsTitleElement);
            contentElement.appendChild(termsAndConditions1Element);

            // append content to template
            templateElement.appendChild(contentElement);

            // append template to root
            rootElement.appendChild(templateElement);

            xmlDocument.appendChild(rootElement);

            return documentToString(xmlDocument);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
