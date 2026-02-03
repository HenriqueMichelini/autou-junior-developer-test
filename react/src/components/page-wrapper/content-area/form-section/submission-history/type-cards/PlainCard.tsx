import { MessageSquareText } from "lucide-react";
import TypeCard from "./TypeCard";

function PlainCard() {
  return (
    <TypeCard
      name="Lorem ipsum, bla bla..."
      size="850 characters"
      icon={<MessageSquareText size={20} />}
      accent="bg-gray-100 text-gray-600"
    />
  );
}

export default PlainCard;
