import { FileType } from "lucide-react";
import TypeCard from "./TypeCard";

function TxtCard() {
  return (
    <TypeCard
      name="Email.txt"
      size="850 KB"
      icon={<FileType size={20} />}
      accent="bg-blue-100 text-blue-600"
    />
  );
}

export default TxtCard;
