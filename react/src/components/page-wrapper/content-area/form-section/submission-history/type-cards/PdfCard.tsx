import { FileText } from "lucide-react";
import TypeCard from "./TypeCard";

function PdfCard() {
  return (
    <TypeCard
      name="contract.pdf"
      size="850 KB"
      icon={<FileText size={20} />}
      accent="bg-red-100 text-red-600"
    />
  );
}

export default PdfCard;
